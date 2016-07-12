package com.rookiefly.test.commons.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by rookiefly on 2015/12/1.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Address {

    private String city;

    private String area;

    private int code = -1;
}
