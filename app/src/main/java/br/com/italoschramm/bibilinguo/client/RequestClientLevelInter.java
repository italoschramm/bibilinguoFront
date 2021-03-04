package br.com.italoschramm.bibilinguo.client;

import br.com.italoschramm.bibilinguo.model.rest.Level;
import br.com.italoschramm.bibilinguo.model.rest.Token;

public interface RequestClientLevelInter {
    void onTaskDone(Level[] level);
}
