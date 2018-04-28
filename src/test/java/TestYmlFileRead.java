import BotPkg.rootPkg.Utils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by OsipovMS on 19.04.2018.
 */
public class TestYmlFileRead {
    @Test
    public void testEnumStatus(){
        Utils u = Utils.INSTANCE;
        String st = u.getJsonRtLoginPassword();
        String tk = u.getTokenTg();
//        Assert.assertThat(u.getTgUserState(new Long(123)), Utils.TgUserState.RootMenu);
        Assert.assertTrue(st != null);
        Assert.assertTrue(tk.length() > 10);

    }
}


