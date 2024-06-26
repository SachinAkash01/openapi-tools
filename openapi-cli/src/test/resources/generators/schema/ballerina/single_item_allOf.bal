public type User record {
    int id?;
    string username?;
    string email?;
    string name?;
    Address address?;
    anydata remarks?;
};

public type Address record {
    string street?;
    string city?;
};

public type description anydata;

public type id Name;

public type Name string;
