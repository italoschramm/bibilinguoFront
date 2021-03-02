package br.com.italoschramm.bibilinguo.client;

import br.com.italoschramm.bibilinguo.model.rest.UserReturn;

public interface RequestClientRegisterUserInter {
    void onTaskDoneRegister(UserReturn userReturn);
}
