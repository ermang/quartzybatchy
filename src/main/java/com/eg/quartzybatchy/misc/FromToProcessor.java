package com.eg.quartzybatchy.misc;


import com.eg.quartzybatchy.entity.FromTable;
import com.eg.quartzybatchy.entity.ToTable;
import org.springframework.batch.item.ItemProcessor;

public class FromToProcessor implements ItemProcessor<FromTable, ToTable> {
    @Override
    public ToTable process(FromTable fromTable) throws Exception {
        ToTable tt = new ToTable();
        tt.setDescr(fromTable.getDescr().concat(" processed"));

        return tt;
    }
}
