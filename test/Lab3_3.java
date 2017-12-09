
public class Lab3_3{
    public static void main(String[] args){
        Developer A = new Developer("Ngo Kien Tuan",20,"Bac Ninh","ASUS",1);
        System.out.println(A.getComputer_brand());
    }
 }

class People{
    String name;
    Integer age;
    String hometown;

    public People(String name, Integer age, String hometown) {
        this.name = name;
        this.age = age;
        this.hometown = hometown;
    }
    
    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getHometown() {
        return hometown;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }
    
}

class Developer extends People{
    String computer_brand;
    Integer job_experience;

    public Developer(String name, Integer age, String hometown, String computer_brand, Integer job_experience) {
        super(name, age, hometown);
        this.computer_brand = computer_brand;
        this.job_experience = job_experience;
    }

    public String getComputer_brand() {
        return computer_brand;
    }

    public Integer getJob_experience() {
        return job_experience;
    }

    public void setComputer_brand(String computer_brand) {
        this.computer_brand = computer_brand;
    }

    public void setJob_experience(Integer job_experience) {
        this.job_experience = job_experience;
    }
 
}

class Teacher extends People{
    String school_name;
    String subject;

    public Teacher(String name, Integer age, String hometown, String school_name, String subject) {
        super(name, age, hometown);
        this.school_name = school_name;
        this.subject = subject;
    }

    public String getSchool_name() {
        return school_name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
     
}

class Grabber extends People{
    String motorbike_brand;

    public Grabber(String name, Integer age, String hometown, String motorbike_brand) {
        super(name, age, hometown);
        this.motorbike_brand = motorbike_brand;
    }

    public String getMotorbike_brand() {
        return motorbike_brand;
    }

    public void setMotorbike_brand(String motorbike_brand) {
        this.motorbike_brand = motorbike_brand;
    }
 
}

class President extends People{
    String country;
    String expire_year;

    public President(String name, Integer age, String hometown, String country, String expire_year) {
        super(name, age, hometown);
        this.country = country;
        this.expire_year = expire_year;
    }

    public String getCountry() {
        return country;
    }

    public String getExpire_year() {
        return expire_year;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setExpire_year(String expire_year) {
        this.expire_year = expire_year;
    }
  
}

class Student extends People{
    String school_name;
    String class_name;

    public Student(String name, Integer age, String hometown, String school_name, String class_name) {
        super(name, age, hometown);
        this.school_name = school_name;
        this.class_name = class_name;
    }

    public String getSchool_name() {
        return school_name;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }
     
}

class Driver extends People{
    String car_brand;
    String company_name;

    public Driver(String name, Integer age, String hometown, String car_brand, String company_name) {
        super(name, age, hometown);
        this.car_brand = car_brand;
        this.company_name = company_name;
    }

    
    public String getCar_brand() {
        return car_brand;
    }

    public void setCar_brand(String car_brand) {
        this.car_brand = car_brand;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
    
}

class CEO extends People{
    String company_name;
    String field;

    public CEO(String name, Integer age, String hometown, String company_name, String field) {
        super(name, age, hometown);
        this.company_name = company_name;
        this.field = field;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
    
    
}

class Football_Player extends People{
    String club_name;
    Integer contract_expire;

    public Football_Player(String name, Integer age, String hometown, String club_name, Integer contract_expire) {
        super(name, age, hometown);
        this.club_name = club_name;
        this.contract_expire = contract_expire;
    }

    public String getClub_name() {
        return club_name;
    }

    public void setClub_name(String club_name) {
        this.club_name = club_name;
    }

    public Integer getContract_expire() {
        return contract_expire;
    }

    public void setContract_expire(Integer contract_expire) {
        this.contract_expire = contract_expire;
    }
    
}

class Pro_Gamer extends People{
    String team_name;
    String game_name;

    public Pro_Gamer(String name, Integer age, String hometown, String team_name, String game_name) {
        super(name, age, hometown);
        this.team_name = team_name;
        this.game_name = game_name;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }
    
}

class Staff extends People{
    String office_name;
    Integer salary;

    public Staff(String name, Integer age, String hometown, String office_name, Integer salary) {
        super(name, age, hometown);
        this.office_name = office_name;
        this.salary = salary;
    }

    public String getOffice_name() {
        return office_name;
    }

    public void setOffice_name(String office_name) {
        this.office_name = office_name;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }
    
}