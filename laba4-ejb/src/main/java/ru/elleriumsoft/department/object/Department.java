package ru.elleriumsoft.department.object;

public class Department
{
    private String nameEmployee;
    private String profession;
    private String employmentDate;

    public Department(String nameEmployee, String profession, String employmentDate)
    {
        this.nameEmployee = nameEmployee;
        this.profession = profession;
        this.employmentDate = employmentDate;
    }

    public String getNameEmployee()
    {
        return nameEmployee;
    }

    public void setNameEmployee(String nameEmployee)
    {
        this.nameEmployee = nameEmployee;
    }

    public String getProfession()
    {
        return profession;
    }

    public void setProfession(String profession)
    {
        this.profession = profession;
    }

    public String getEmploymentDate()
    {
        return employmentDate;
    }

    public void setEmploymentDate(String employmentDate)
    {
        this.employmentDate = employmentDate;
    }
}
