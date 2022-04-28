package za.co.spsi.mdms.common.db.generator;

import za.co.spsi.toolkit.db.View;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldLocalDate;

import java.sql.Timestamp;

import static za.co.spsi.mdms.common.db.generator.GeneratorTransactionEntity.Status.Active;

/**
 * Created by jaspervdbijl on 2017/07/26.
 */
public class GeneratorTransactionDetailView extends View {

    public GeneratorTransactionEntity generatorTx = new GeneratorTransactionEntity();
    public GeneratorTransactionEntity.DetailLine detailLine = new GeneratorTransactionEntity.DetailLine();

    public FieldLocalDate<Timestamp> txStart = generatorTx.txStart;
    public FieldLocalDate<Timestamp> txEnd = generatorTx.txEnd;
    public Field<String> kamMeterId = detailLine.kamMeterId;
    public Field<String> nesMeterId = detailLine.nesMeterId;

    public GeneratorTransactionDetailView() {

        setSql("select gen_transaction.* from gen_transaction " +
                "left join gen_tx_detail on gen_transaction.id = gen_tx_detail.gen_tx_id " +
                "where " +
                "gen_transaction.status = ? and gen_transaction.tx_start > sysdate - 1)",Active.code);
    }

}
