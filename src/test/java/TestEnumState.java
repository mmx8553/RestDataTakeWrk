import EmptyBot.TgUserState;
import EmptyBot.Utils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by OsipovMS on 11.04.2018.
 */
public class TestEnumState {
    @Test
    public void testEnumStatus(){
        Utils u = new Utils();
        u.setTgUserSate(new Long(123), TgUserState.RootMenu);
        u.setTgUserSate(new Long(123), TgUserState.ObjectSelectMenu);
//        Assert.assertThat(u.getTgUserState(new Long(123)), Utils.TgUserState.RootMenu);
        Assert.assertTrue(u.getTgUserState(new Long(123)).equals( TgUserState.ObjectSelectMenu));
    }
}
