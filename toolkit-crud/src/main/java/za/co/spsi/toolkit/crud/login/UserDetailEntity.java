/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package za.co.spsi.toolkit.crud.login;


import za.co.spsi.toolkit.crud.util.CrudUAHelper;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.FormattedSql;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.uaa.util.dto.TokenResponseDao;
import za.co.spsi.uaa.util.dto.User;

import javax.sql.DataSource;
import java.sql.Timestamp;

@Table(version = 5)
public class UserDetailEntity extends EntityDB {

    @Id()
    @Column(name = "USER_NAME", size = 50)
    public Field<String> username = new Field<>(this);

    @Column(name = "FIRST_NAME", size = 50)
    public Field<String> firstName = new Field<>(this);

    @Column(name = "LAST_NAME", size = 50)
    public Field<String> lastName = new Field<>(this);


    @Column(name = "INITIALS", size = 10)
    public Field<String> initials = new Field<>(this);

    public Field<String> ou = new Field<>(this);

    @Column(name = "METRIC_TOTAL")
    public Field<Integer> metricTotal = new Field<>(this);

    @Column(name = "LAST_LOGIN")
    public Field<Timestamp> lastLogin = new Field<>(this);

    public UserDetailEntity() {
        super("USER_DETAIL");
    }


    public static String getUsernameJoinColumn() {
        return "UPPER(User_Detail.FIRST_NAME || ' ' || User_Detail.LAST_NAME) as userName ";
    }

    public static String formatSql(String sqlString, String userField) {
        FormattedSql sql = new FormattedSql(sqlString);
        sql.setSelect(sql.getSelect() + ", " + getUsernameJoinColumn());
        sql.setFrom(sql.getFrom() + String.format(" left join User_Detail on TRIM(UPPER(%s)) = TRIM(User_Detail.user_name) ", userField));
        return sql.toString();
    }

    public UserDetailEntity init(User user, TokenResponseDao dao) {
        this.username.set(user.getUid());
        this.firstName.set(user.getCommonName());
        this.lastName.set(user.getSurname());
        this.ou.set(user.getOrganizationalUnit());
        this.lastLogin.set(new Timestamp(System.currentTimeMillis()));
        return this;
    }

    public UserDetailEntity init(User user) {
        this.username.set(user.getUid());
        this.firstName.set(user.getCommonName());
        this.lastName.set(user.getSurname());
        return this;
    }

    public static UserDetailEntity init(DataSource dataSource, CrudUAHelper uaHelper, LoginView.LoginEventRequest request,
                                        TokenResponseDao dao) {
        User user = uaHelper.getUserDetail(request.getUsername(), dao.getAccessToken());
        UserDetailEntity userDetail = (UserDetailEntity) DataSourceDB.getFromSet(dataSource,
                (EntityDB) new UserDetailEntity().username.set(user.getUid()));
        DataSourceDB.set(dataSource,userDetail = (userDetail == null ? new UserDetailEntity().init(user,dao):userDetail.init(user,dao)));
        return userDetail;
    }
}
