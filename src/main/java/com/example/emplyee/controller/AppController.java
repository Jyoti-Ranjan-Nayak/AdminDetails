package com.example.emplyee.controller;

import com.example.emplyee.model.Employee;
import com.example.emplyee.model.Admin;
import com.example.emplyee.repository.EmployeeRepository;
import com.example.emplyee.repository.AdminRepository;
import com.example.emplyee.sevice.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AppController {

    private final EmployeeRepository employeeRepository;
    private final AdminRepository adminRepository;
    private final EmailService emailService;

    @Autowired
    public AppController(EmployeeRepository employeeRepository, AdminRepository adminRepository, EmailService emailService) {
        this.employeeRepository = employeeRepository;
        this.adminRepository = adminRepository;
        this.emailService = emailService;
    }

    // Login page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Dashboard: show all employees (and optionally admins)
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("admins", adminRepository.findAll());
        return "dashboard";
    }

 // Search Admins (corrected)
    @GetMapping("/dashboard/search-admins")
    public String searchAdmins(@RequestParam("keyword") String keyword, Model model) {
        List<Admin> results = adminRepository.findByUsernameContainingIgnoreCase(keyword);
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("admins", results);
        return "dashboard";
    }

    // Search Employees
    @GetMapping("/dashboard/search-employees")
    public String searchEmployees(@RequestParam("keyword") String keyword, Model model) {
        List<Employee> employees = employeeRepository.findByKeyword(keyword);
        model.addAttribute("employees", employees);
        model.addAttribute("searchPerformed", true);
        return "dashboard"; // Thymeleaf template name
    }

    

    // Add new employee form
    @GetMapping("/add-employee")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee-form";
    }

    // Handle employee creation
    @PostMapping("/add-employee")
    public String saveEmployee(@ModelAttribute("employee") Employee employee, RedirectAttributes redirectAttributes) {
        employeeRepository.save(employee);
        emailService.sendSuccessEmail(employee.getEmployeeEmail(), employee.getEmployeeName());
        redirectAttributes.addFlashAttribute("successMessage",
                "Employee added and email sent successfully to " + employee.getEmployeeEmail());
        return "redirect:/dashboard";
    }

    // Edit employee form
    @GetMapping("/edit-employee/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID: " + id));
        model.addAttribute("employee", employee);
        return "edit-employee";
    }

    // Update employee
    @PostMapping("/update-employee/{id}")
    public String updateEmployee(@PathVariable Long id, @ModelAttribute("employee") Employee updatedEmployee,
                                 RedirectAttributes redirectAttributes) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID: " + id));

        existing.setEmployeeName(updatedEmployee.getEmployeeName());
        existing.setEmployeeId(updatedEmployee.getEmployeeId());
        existing.setEmployeeNumber(updatedEmployee.getEmployeeNumber());
        existing.setEmployeeEmail(updatedEmployee.getEmployeeEmail());

        employeeRepository.save(existing);
        emailService.sendSuccessEmail(existing.getEmployeeEmail(), existing.getEmployeeName());

        redirectAttributes.addFlashAttribute("successMessage",
                "Employee updated and email sent successfully to " + existing.getEmployeeEmail());
        return "redirect:/dashboard";
    }

    // Delete employee
    @GetMapping("/delete-employee/{id}")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (!employeeRepository.existsById(id)) {
            throw new IllegalArgumentException("Invalid employee Id: " + id);
        }
        employeeRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully.");
        return "redirect:/dashboard";
    }
}