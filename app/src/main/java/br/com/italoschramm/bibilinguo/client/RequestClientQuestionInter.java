package br.com.italoschramm.bibilinguo.client;

import br.com.italoschramm.bibilinguo.model.rest.Level;
import br.com.italoschramm.bibilinguo.model.rest.Question;

public interface RequestClientQuestionInter {

    void onTaskDone(Question[] questions);
}
