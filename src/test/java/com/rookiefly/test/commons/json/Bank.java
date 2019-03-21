package com.rookiefly.test.commons.json;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by rookiefly on 2015/12/1.
 */
@Data
@AllArgsConstructor
public class Bank {

    private String bankId;

    private String iconUrl;

    private Account account;
}
