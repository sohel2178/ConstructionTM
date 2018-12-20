package com.forbitbd.constructiontm.ui.searchUser;


import com.forbitbd.constructiontm.model.User;

import java.util.List;

public interface SearchUserContract {

    interface Presenter{
        void requestForData(String value, int before, int after);
    }

    interface View{
        void updateData(List<User> userList, String val);
        void updateAdapter(String value);
        void userSelect(User user);
    }
}
