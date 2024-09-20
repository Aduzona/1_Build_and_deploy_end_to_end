package com.codedecode.order.service;

import com.codedecode.order.entity.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import static org.springframework.data.mongodb.core.query.Criteria.where;



@Service
public class SequenceGenerator {

    @Autowired
    MongoOperations mongoOperations;

    public int generateNextOrderId(){
        Sequence counter= mongoOperations.findAndModify(
                Query.query(where("_id").is("sequence")),
                new Update().inc("sequence", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                Sequence.class
        );
        return  counter.getSequence();
    }
}
