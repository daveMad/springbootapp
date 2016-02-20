package com.cool;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import antlr.collections.List;


@Configuration
@ComponentScan
@EnableAutoConfiguration
public class SocoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocoolApplication.class, args);

	}

}



interface CompanyRepository extends JpaRepository<Company, Long> {
	Collection<Company> getByCompanyname(String companyname);
}

interface BeneficialOwnerRepository extends JpaRepository<Beneficialowner, Long> {
	Collection<Beneficialowner> getByName(String name);

	Collection<Beneficialowner> getByCompanyid(Long companyid);
}


@RestController
class CoolRestController {
	ArrayList<CompanyDto> getCompanyDtos() {
		ArrayList<CompanyDto> dtos = new ArrayList<>();

		ArrayList<Company> comps = (ArrayList<Company>) this.companyRepository.findAll();
		ArrayList<Beneficialowner> benefs = (ArrayList<Beneficialowner>) this.beneficialOwnerRepository.findAll();
		for (Company c : comps) {
			CompanyDto d = new CompanyDto();
			d.setId(c.getId());
			d.setCompanyname(c.getCompanyname());
			d.setAdress(c.getAdress());
			d.setCity(c.getCity());
			d.setCountry(c.getCountry());
			d.setEmail(c.getEmail());
			d.setPhone(c.getPhone());
			for (Beneficialowner b : benefs) {
				if (b.getCompanyid() == c.getId()) {
					d.beneficialOwners.add(b.getName());
				}
			}
			dtos.add(d);
		}
		return dtos;
	}
	
	CompanyDto getOneCompany(long id){
		// get company and it's owners
		Company company = this.companyRepository.findOne(id);
		ArrayList<Beneficialowner> benefs = (ArrayList<Beneficialowner>) this.beneficialOwnerRepository.getByCompanyid(id);
		
		// initialize companyDto instance fill the company data and add owners
		CompanyDto companyDto = new CompanyDto();
		companyDto.setId(company.getId());
		companyDto.setAdress(company.getAdress());
		companyDto.setCity(company.getCity());
		companyDto.setCompanyname(company.getCompanyname());
		companyDto.setCountry(company.getCountry());
		companyDto.setEmail(company.getEmail());
		companyDto.setPhone(company.getPhone());
		// add each owner here
		for(Beneficialowner b : benefs){
			companyDto.beneficialOwners.add(b.getName());
		}
		
		return companyDto;
	}
	
	@RequestMapping(value="/companito/{id}",method=RequestMethod.GET)
	CompanyDto getCompanyById(@PathVariable long id){
		return getOneCompany(id);
	}
	
	
	

	@RequestMapping("/companies")
	ArrayList<CompanyDto> companies() {

		return getCompanyDtos();
	}
	
	@RequestMapping(value="/company",method=RequestMethod.POST)
	ArrayList<CompanyDto> updateCompany(@RequestBody Company company){
		// find the company by it's id and change it, flush db
		Company current = this.companyRepository.findOne(company.getId());
		// update the values one by one
		current.setAdress(company.getAdress());
		current.setCity(company.getCity());
		current.setCompanyname(company.getCompanyname());
		current.setCountry(company.getCountry());
		current.setEmail(company.getEmail());
		current.setPhone(company.getPhone());
		current.setId(company.getId());
		
		this.companyRepository.saveAndFlush(current);
		return getCompanyDtos();
	}

	@RequestMapping("/owners")
	ArrayList<Beneficialowner> owners() {
		
		return (ArrayList<Beneficialowner>) this.beneficialOwnerRepository.findAll();
	}

	@RequestMapping("/newowner")
	ArrayList<CompanyDto> addNewOwner(@RequestBody ArrayList<Beneficialowner> newowner) {
		//long companyId = newowner.get(0).getCompanyid();
		
		beneficialOwnerRepository.save(newowner);
		beneficialOwnerRepository.flush();
		return getCompanyDtos();

	}
	@RequestMapping(value="/newowner/{companyid}",method=RequestMethod.POST)
	ArrayList<CompanyDto> addNewOwnerByCompanyId(@PathVariable long companyid,@RequestBody String ownerName){
		// Define new BeneficilOwner instance
		Beneficialowner newOwner = new Beneficialowner();
		newOwner.setCompanyid(companyid);
		newOwner.setName(ownerName);
		// add and save it to db
		this.beneficialOwnerRepository.saveAndFlush(newOwner);
		
		
		return getCompanyDtos();
	}

	@RequestMapping(value = "/deletecompany", method = RequestMethod.DELETE)
	ArrayList<CompanyDto> deleteCompany(@RequestParam(value = "id", defaultValue = "0") Long id) {
		companyRepository.delete(id);
		companyRepository.flush();
		// delete beneficial owners whose companyid's are equals to newly
		// deleted company's id
		for (Beneficialowner b : beneficialOwnerRepository.getByCompanyid(id)) {
			beneficialOwnerRepository.delete(b);
			beneficialOwnerRepository.flush();
		}

		return getCompanyDtos();
	}

	@RequestMapping(value = "/newcompany", method = RequestMethod.POST)
	Collection<Company> addNewCompany(@RequestBody Company newcompany) {
		companyRepository.saveAndFlush(newcompany);
		return this.companyRepository.findAll();
	}

	@Autowired
	CompanyRepository companyRepository;
	@Autowired
	BeneficialOwnerRepository beneficialOwnerRepository;
}

class CompanyDto 
{
	private Long id;
	private String companyname;
	private String adress;
	private String city;
	private String country;
	private String email;
	private String phone;
	public ArrayList<String> beneficialOwners;

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public Collection<String> getBeneficialOwners() {
		return beneficialOwners;
	}

	public void setBeneficialOwners(ArrayList<String> beneficialOwners) {
		this.beneficialOwners = beneficialOwners;
	}

	public CompanyDto(Long id, String companyname, ArrayList<String> beneficialOwners) {
		super();
		this.id = id;
		this.companyname = companyname;
		this.beneficialOwners = beneficialOwners;
	}

	public CompanyDto(Long id, String companyname, String adress, String city, String country, String email, String phone,
			ArrayList<String> beneficialOwners) {
		super();
		this.id = id;
		this.companyname = companyname;
		this.adress = adress;
		this.city = city;
		this.country = country;
		this.email = email;
		this.phone = phone;
		this.beneficialOwners = beneficialOwners;
	}

	public CompanyDto() {
		super();
		this.beneficialOwners = new ArrayList<>();
	}
}

@Entity
class Company {
	@Id
	@GeneratedValue
	private Long id;
	@NotNull
	private String companyname;
	@NotNull
	private String adress;
	@NotNull
	private String city;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	@NotNull
	private String country;
	private String email;
	private String phone;

	public Company() {
		super();
	}

	public Company(String companyname, String adress, String city, String country) {
		super();
		this.companyname = companyname;
		this.adress = adress;
		this.city = city;
		this.country = country;
		this.email = "";
		this.phone = "";
	}
	
	

	public Company(String companyname, String adress, String city, String country, String email, String phone) {
		super();
		this.companyname = companyname;
		this.adress = adress;
		this.city = city;
		this.country = country;
		this.email = email;
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", companyname=" + companyname + ", adress=" + adress + ", city=" + city
				+ ", country=" + country + ", email=" + email + ", phone=" + phone + "]";
	}

}

@Entity
class Beneficialowner {
	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private Long companyid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCompanyid() {
		return companyid;
	}

	public void setCompanyid(Long companyid) {
		this.companyid = companyid;
	}

	public Beneficialowner() {
		super();
	}

	public Beneficialowner(String name, Long companyid) {
		super();
		this.name = name;
		this.companyid = companyid;
	}

	@Override
	public String toString() {
		return "Beneficialowner [id=" + id + ", name=" + name + ", companyid=" + companyid + "]";
	}

}