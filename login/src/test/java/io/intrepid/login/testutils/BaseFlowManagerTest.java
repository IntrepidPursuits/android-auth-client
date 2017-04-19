package io.intrepid.login.testutils;

import android.support.annotation.CallSuper;

import org.junit.After;
import org.junit.Before;
import org.mockito.Mock;

import io.intrepid.login.base.LoginFlowCallbacks;
import io.intrepid.login.base.LoginFlowManager;
import io.intrepid.login.base.LoginService;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.TestScheduler;

public class BaseFlowManagerTest<F extends LoginFlowManager> extends BaseUnitTest {

    @Mock
    protected LoginService<MockResponseObject> mockLoginService;
    @Mock
    protected LoginFlowCallbacks<MockResponseObject> mockLoginFlowCallbacks;

    protected F loginFlowManager;
    protected final TestScheduler testScheduler = new TestScheduler();

    @CallSuper
    @Before
    public void setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> testScheduler);
        RxAndroidPlugins.setMainThreadSchedulerHandler(scheduler -> testScheduler);
    }

    @After
    public void baseTeardown() {
        RxAndroidPlugins.reset();
    }
}
