/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.spsi.toolkit.crud.db;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import za.co.spsi.toolkit.crud.db.audit.AuditEntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.entity.Field;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


public abstract class AbstractPhotoEntity extends AuditEntityDB {

    @Column(notNull = true)
    public Field<byte[]> photo = new Field<>(this);

    public Field<String> notes = new Field<>(this);

    public AbstractPhotoEntity(String name) {
        super(name);
    }

    public void createFileOnShare(String fileName, NtlmPasswordAuthentication auth) throws IOException {
        InputStream in = new ByteArrayInputStream(photo.get());
        BufferedImage bImageFromConvert = ImageIO.read(in);
        SmbFile smb = null;
        try {
            smb = new SmbFile(fileName, auth);
            SmbFileOutputStream sops = new SmbFileOutputStream(smb);
            ImageIO.write(bImageFromConvert, "jpg", sops);
        } catch (Exception ex) {
            smb.delete();
            throw ex;
        }
    }
}
