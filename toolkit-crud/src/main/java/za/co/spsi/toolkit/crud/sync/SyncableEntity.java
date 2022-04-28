package za.co.spsi.toolkit.crud.sync;

import za.co.spsi.toolkit.crud.sync.db.BaseSharedSyncEntity;

/**
 * Created by jaspervdbijl on 2017/03/15.
 */
public interface SyncableEntity {

    BaseSharedSyncEntity getBaseSharedSyncEntity();
}
