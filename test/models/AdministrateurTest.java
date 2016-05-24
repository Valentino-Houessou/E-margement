package models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by Miage on 19/05/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class AdministrateurTest {

    @Mock
    Utilisateur utilisateur;

    @Test
    public void testCreate()throws Exception{
        /*Utilisateur userMock = mock(Utilisateur.class);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date ddnd = formatter.parse("2016-03-17 01:58:00");
        Timestamp ddn = new Timestamp(ddnd.getTime());
        Utilisateur user = new Utilisateur("test", "test", "test", "test", ddn, "test");
        when(userMock.create(anyString(), anyString(),anyString(), anyString(), anyString(), anyString())).thenReturn(user);

        Administrateur administrateur = Administrateur.create("test", "test", "test", "test", "test", "", "test", true);

        assertNotNull(administrateur.sonUtilisateur);
        assertEquals(user, administrateur.sonUtilisateur);*/

    }
}