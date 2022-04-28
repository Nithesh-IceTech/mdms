package za.co.spsi.toolkit.crud.gui.custom;

import com.vaadin.server.*;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import za.co.spsi.Canvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by jaspervdb on 2015/11/03.
 */
public class ImageCanvas extends VerticalLayout {

    public enum State {
        Line, Text, None
    }

    public static final double LINE_WIDTH = 3;

    public static final Logger TAG = Logger.getLogger(ImageCanvas.class.getName());

    private Canvas canvas = new Canvas();
    private String imageId = UUID.randomUUID().toString() + ".jpg";
    private Button draw = new Button(FontAwesome.EDIT), undo = new Button(FontAwesome.UNDO),
            clear = new Button(FontAwesome.TRASH_O);

    private boolean displayEditButtons = true;
    private List<Point> points = new ArrayList<>();
    private List<Point> textPoints = new ArrayList<>();
    private List<String> drawnTextList = new ArrayList<>();

    private State state = State.Line;
    private String textToDraw;

    private int width, height;
    private BufferedImage originalImage, image;

    public ImageCanvas(int width, int height) {
        setSizeUndefined();
        this.width = width;
        this.height = height;
    }

    /**
     * overload to add functionality
     *
     * @return
     */
    public Button[] getButtons() {
        return new Button[]{};
    }

    public void setEditButtons(boolean displayEditButtons) {
        this.displayEditButtons = displayEditButtons;
    }

    public void drawText(String text) {
        state = State.Text;
        textToDraw = text;
    }

    public void removeText(String text) {
        state = State.None;
        for (int i = 0; i < drawnTextList.size(); i++) {
            if (text.equals(drawnTextList.get(i))) {
                drawnTextList.remove(i);
                textPoints.remove(i);
                i--;
            }
        }
        redraw();
    }

    private HorizontalLayout createBtnGroup() {
        ToggleButtonGroup btnGroup = new ToggleButtonGroup(draw);
        Button buttons[] = getButtons();
        btnGroup.addComponents(buttons);
        btnGroup.addButton(undo, false);
        btnGroup.addButton(clear, false);
        btnGroup.setSelected(draw);
        buttons[buttons.length - 1].setWidth("100%");
        btnGroup.setExpandRatio(buttons[buttons.length - 1], 2f);
        undo.setEnabled(false);
        undo.setVisible(displayEditButtons);
        clear.setVisible(displayEditButtons);
        draw.setVisible(displayEditButtons);
        btnGroup.setWidth("100%");
        return btnGroup;
    }

    private String getImageUrl() {
        return String.format("%s/%s", UI.getCurrent().getPage().getLocation().getScheme() + ":" + UI.getCurrent().getPage().getLocation().getSchemeSpecificPart(), imageId);
    }

    public void init(final BufferedImage bImg) {
        try {
            this.originalImage = bImg;
            this.image = new BufferedImage(800, 600, BufferedImage.TYPE_3BYTE_BGR);
            this.image.getGraphics().drawImage(this.originalImage.getScaledInstance(800, 600, Image.SCALE_SMOOTH), 0, 0, null);

            canvas.setWidth(width + "");
            canvas.setHeight(height + "");

            final RequestHandler requestHandler = new RequestHandler() {
                @Override
                public boolean handleRequest(VaadinSession vaadinSession, VaadinRequest vaadinRequest, VaadinResponse vaadinResponse) throws IOException {
                    if (vaadinRequest.getPathInfo().endsWith(imageId)) {
                        vaadinResponse.setContentType("image/jpeg");
                        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ImageIO.write(ImageCanvas.this.image, "jpg", bos);
                        vaadinResponse.setContentLength(bos.toByteArray().length);
                        vaadinResponse.getOutputStream().write(bos.toByteArray());
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            VaadinSession.getCurrent().addRequestHandler(requestHandler);


            TAG.info(String.format("Image URL: %s",getImageUrl()));
            canvas.loadImages(new String[]{getImageUrl()});

            canvas.addImageLoadListener(new Canvas.CanvasImageLoadListener() {
                @Override
                public void imagesLoaded() {
                    drawImage();
                }
            });

            addDetachListener(new DetachListener() {
                @Override
                public void detach(DetachEvent detachEvent) {
                    VaadinSession.getCurrent().removeRequestHandler(requestHandler);
                }
            });
            addComponent(canvas);

            addComponent(createBtnGroup());

            setExpandRatio(canvas, 2f);
            draw.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    state = State.Line;
                }
            });
            undo.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    if (!points.isEmpty()) {
                        points.remove(points.size() - 1);
                        redraw();
                    }
                    undo.setEnabled(!points.isEmpty());
                }
            });
            clear.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
//                    canvas.closePath();
                    drawnTextList.clear();
                    textPoints.clear();
                    points.clear();
                    drawImage();
                }
            });
            if (displayEditButtons) {
                canvas.addMouseUpListener(new Canvas.CanvasMouseUpListener() {
                    @Override
                    public void onMouseUp(MouseEventDetails event) {
                        if (state == State.Line) {
                            points.add(new Point(event.getRelativeX(), event.getRelativeY()));
                            drawLines();
                            undo.setEnabled(true);
                        } else if (state == State.Text) {
                            textPoints.add(new Point(event.getRelativeX(), event.getRelativeY()));
                            drawnTextList.add(textToDraw);
                            drawText(textToDraw, event.getRelativeX(), event.getRelativeY());
                        }

                    }

                });
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void drawText(String text, double x, double y) {
        canvas.setLineWidth(LINE_WIDTH);
        canvas.setFont("bold 30px Verdana");
        canvas.setFillStyle("yellow");
        int i = 0;
        for (String value : text.split("\n")) {
            canvas.fillText(value, x, y + (i++ * 30) + 5, width * 1d);
        }
    }

    private void redraw() {
        drawImage();
        List<Point> pointList = new ArrayList<>();
        for (Point p : points) {
            pointList.add(p);
            drawLines(pointList);
        }
        for (int i = 0; i < textPoints.size(); i++) {
            drawText(drawnTextList.get(i), textPoints.get(i).getX(), textPoints.get(i).getY());
        }
    }

    private void drawCircle(double x, double y, int radius) {
        canvas.moveTo(x, y);
        canvas.arc(x, y, radius * 1d, 0d, 2 * Math.PI, false);
        canvas.setLineWidth(LINE_WIDTH);
        canvas.setStrokeStyle("red");
        canvas.stroke();
        canvas.moveTo(x, y);
    }

    private void drawLines() {
        drawLines(points);
    }

    private void drawLines(List<Point> points) {
        if (!points.isEmpty()) {
            if (points.size() > 1) {
                canvas.lineTo(points.get(points.size() - 1).getX(), points.get(points.size() - 1).getY());
            }
            drawCircle(points.get(points.size() - 1).getX(), points.get(points.size() - 1).getY(), 3);
        }
    }

    private void drawImage() {
        canvas.moveTo(0, 0);
        double x = 0, y = 0;
        canvas.drawImage2(getImageUrl(), x, y, width * 1d, height * 1d);
        canvas.beginPath();
    }

    private Point getNorm(Point p) {
        double nx = image.getWidth() * 1d / width, ny = image.getHeight() * 1d / height;
        float mx = width * 1f / 2, my = height * 1f / 2;
        double x = (mx - p.getX()) * nx, y = (my - p.getY()) * ny;
        return new Point((int) Math.round((image.getWidth() / 2) - x), (int) Math.round((image.getHeight() / 2) - y));
    }

    /**
     * will create a new image from the canvas
     *
     * @return
     */
    public BufferedImage getAsBuffered() {
        double nx = image.getWidth() / width, ny = image.getHeight() / height;
        BufferedImage tmpImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) tmpImage.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.RED);
        g.setBackground(Color.RED);
        g.drawImage(image, 0, 0, null);
        int lineWidth = (int) (LINE_WIDTH * nx);
        g.setStroke(new BasicStroke(lineWidth));
        for (int i = 0; i < points.size(); i++) {
            Point p1 = getNorm(points.get(i));
            if (i < points.size() - 1) {
                Point p2 = getNorm(points.get(i + 1));
                g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) (p2.getY()));
            }
            g.drawOval((int) (p1.getX() - lineWidth), (int) (p1.getY() - lineWidth), lineWidth * 2, lineWidth * 2);
            g.fillOval((int) (p1.getX() - lineWidth), (int) (p1.getY() - lineWidth), lineWidth * 2, lineWidth * 2);
        }
        for (int i = 0; i < textPoints.size(); i++) {
            Point p = getNorm(textPoints.get(i));
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Verdana", Font.BOLD, (int) (30 * nx)));
            int y = 0;
            for (String str : drawnTextList.get(i).split("\n")) {
                g.drawString(str, (int) p.getX(), (int) p.getY() + y++ * 30);
            }
        }
        return tmpImage;
    }

}
