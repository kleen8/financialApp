package nl.sogyo.financialApp;

public enum AccountType {
    GENERAL("GeneralAccount", GeneralAccount.class),
    SAVINGS("SavingsAccount", SavingsAccount.class),
    INVESTMENTS("InvestmentsAccount", InvestmentsAccount.class);

    private final String typeName;
    private final Class<? extends Account> accountClass;

    AccountType(String typeName, Class<? extends Account> accountClass) {
        this.typeName = typeName;
        this.accountClass = accountClass;
    }

    public String getTypeName() {
        return typeName;
    }

    public Class<? extends Account> getAccountClass(){
        return accountClass;
    }

    public static AccountType fromTypeName(String typeName){
        for (AccountType type : values()) {
            if (type.typeName.equals(typeName)){
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown account type" + typeName);
    }
}
