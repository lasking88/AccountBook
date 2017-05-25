package com.ohbrothers.www.accountbook.database;

/**
 * Created by jk on 5/15/17.
 */

public class DbSchema {
    public static final class DataTable {
        public static final String NAME = "inoutcomes";

        public static final class Cols {
            public static final String DATE = "date";
            public static final String DETAIL = "detail";
            public static final String INOUTCOME = "inoutcome";
        }
    }

    public static final class PasscodeTable {
        public static final String NAME = "passcode";

        public static final class Cols {
            public static final String PASSCODESWITCH = "switch";
            public static final String PASSCODE = "passcode";
        }
    }
}
