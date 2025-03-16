import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Timetable{

    private static final int NUM_DAYS_IN_WEEK = 5; // Assuming timetable is for 5 days in a week
    private static final String[] DAYS_OF_WEEK = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private static final String TIMETABLE_FILE_1 = "timetable1.txt";
    private static final String TIMETABLE_FILE_2 = "timetable2.txt";
    private static final String TIMETABLE_FILE_3 = "timetable3.txt";
    private static final String TIMETABLE_FILE_4 = "timetable4.txt";
    private static final int numofclasses =4;


    private static Scanner scanner = new Scanner(System.in);
    private static List<String> timetable = new ArrayList<>(); // Storing timetable

    public static void main(String[] args) {
        System.out.println("______________ Welcome to Timetable Generator ________________\n");

        while (true) {
            System.out.println("1. View Timetable");
            System.out.println("2. Create Timetable");
            System.out.println("3. Modify Timetable");
            System.out.println("4. Exit");
            System.out.println("Select an option:");
            int choice = getIntInput(1, 4);
            switch (choice) {
                case 1:
                    viewTimetable();
                    break;
                case 2:
                    createTimetable();
                    break;
                case 3:
                    modifyTimetableFromFile();
                    break;
                case 4:
                    System.out.println("Exiting program...");
                    return;
            }
        }
    }
    
    private static void createTimetable() {
        System.out.println("Create Timetable option selected");

        System.out.println("Enter the Department name (in CAPS):");
        String courseName = scanner.next();

        Scanner inputs = new Scanner(System.in);
        int yearOfStudy;

        switch (courseName) {
            case "BTECH":
                System.out.print("Enter the year of study of students (1-4): ");
                yearOfStudy = getIntInput(1, 4);
                break;
            case "MTECH":
                System.out.print("Enter the year of study of students (1-2): ");
                yearOfStudy = getIntInput(1, 2);
                break;
            case "PHD":
                System.out.print("Enter the year of study of students (1-5): ");
                yearOfStudy = getIntInput(1, 5);
                break;
            case "B.COM":
                System.out.print("Enter the year of study of students (1-3): ");
                yearOfStudy = getIntInput(1, 3);
                break;
            case "BBA":
                System.out.print("Enter the year of study of students (1-3): ");
                yearOfStudy = getIntInput(1, 3);
                break;
            case "BCA":
                System.out.print("Enter the year of study of students (1-3): ");
                yearOfStudy = getIntInput(1, 3);
                break;
            default:
                return;
        }

        System.out.println("Enter the number of faculty for the department:");
        int facultyCount = inputs.nextInt();

        // Create an array to store the names of faculty members
        String[] faculties = new String[facultyCount];

        // Prompt the user to enter the names of faculty members
        for (int b = 0; b < facultyCount; b++) {
            System.out.println("Enter the name of faculty " + (b + 1) + ":");
            faculties[b] = inputs.next();
        }

        System.out.print("Enter number of subjects: ");
        int numSubjects = inputs.nextInt();


        List<List<Integer>> facultyIndices = new ArrayList<>();

        List<Subjects> subjects = new ArrayList<>();
        for (int i = 0; i < numSubjects; i++) {
            System.out.println("\nEnter details for Subject " + (i + 1) + ":");
            System.out.print("Enter subject name: ");
            String name = scanner.nextLine();

            System.out.print("Enter number of units in subject: ");
            int units = getIntInput(1, Integer.MAX_VALUE);

            System.out.print("Enter estimated time of completion (in days): ");
            int days = getIntInput(1, Integer.MAX_VALUE);

            System.out.println("Enter the credits of the subject:");
            int credits = 2 * getIntInput(1, 5);

            // Prompt the user to select the faculty members for this subject
            List<Integer> facultyIndicesForSubject = new ArrayList<>();
            System.out.println("Select the faculty members for Subject " + (i + 1) + ":");
            for (int c = 0; c < facultyCount; c++) {
                System.out.println(c + ": " + faculties[c]);
            }

            // Continue adding faculty members until the user finishes
            System.out.println("Enter the indices of the faculty members (separated by spaces, -1 to finish):");
            int facultyIndex;
            while ((facultyIndex = inputs.nextInt()) != -1) {
                facultyIndicesForSubject.add(facultyIndex);
            }

            // Store the faculty indices for this subject
            facultyIndices.add(facultyIndicesForSubject);

            // Convert faculty indices to names and create the subject object
            String[] selectedTeachers = new String[facultyIndicesForSubject.size()];
            for (int j = 0; j < facultyIndicesForSubject.size(); j++) {
                selectedTeachers[j] = faculties[facultyIndicesForSubject.get(j)];
            }

            // Create the subject object and add it to the list
            subjects.add(new Subjects(name, units, days, credits, selectedTeachers));
        }


        System.out.println("\nSubject-wise Faculty Assignments:");
        for (int s = 0; s < subjects.size(); s++) {
            Subjects subject = subjects.get(s);
            System.out.println(subject.getName() + ":");
            System.out.println("\tFaculty:");
            String[] facultyForSubject = subject.getTeachers();
            for (String faculty : facultyForSubject) {
                System.out.println("\t\t" + faculty);
            }
        }

        generateTimetable(subjects);
        System.out.println("Timetable generated. Now writing to file...");


        viewTimetable();
    }

    
	private static void generateTimetable(List<Subjects> allSubjects) {
		
		
		int Slotsperday=7;
	    System.out.println("\nGenerating timetable...\n");
	    
	    // Initialize timetable slots for each class
	    List<List<List<String>>> classSlots = new ArrayList<>();
	    for (int i = 0; i < numofclasses; i++) {
	        List<List<String>> slots = new ArrayList<>();
	        for (int j = 0; j < NUM_DAYS_IN_WEEK; j++) {
	            slots.add(new ArrayList<>());
	        }
	        classSlots.add(slots);
	    }
	    
	    // Initialize subjects for each class
	    List<List<Subjects>> classSubjects = new ArrayList<>();
	    for (int i = 0; i < numofclasses; i++) {
	        classSubjects.add(new ArrayList<>(allSubjects));
	    }

	    // Generate timetable for each class
	    for (int a = 0; a < numofclasses; a++) {
	        List<Subjects> subjects = classSubjects.get(a);
	        
	        // Calculate the total credits for this class
	        int totalCredits = subjects.stream().mapToInt(Subjects::getCredits).sum();
	        
	        for (int i = 0; i < NUM_DAYS_IN_WEEK; i++) {
	            for (int j = 0; j < Slotsperday; j++) {
	                // Your existing logic here to allocate subjects to slots
	            	
	                    // If no subjects left, fill with standard breaks
	                    if ((j == 2 || j == 7)) { // Check if it's time for a 10-minute break
	                        classSlots.get(a).get(i).add("10-Min Break");
	                    } else if ((j == 4)) { // Check if it's time for lunch break
	                        classSlots.get(a).get(i).add("Lunch Break");
	                    }
	                    else if(i==2 && j==0 || i==4 &&j==1){
	                        classSlots.get(a).get(i).add("Break");
	                    }
	                
	                
	                // Allocate subjects if there are still subjects left for this class
	                if (!subjects.isEmpty()) {
	                    // Sort subjects by credits in descending order
	                    subjects.sort(Comparator.comparingInt(Subjects::getCredits).reversed());

	                    // Calculate weightage based on subject credits
	                    double weightage = (double) subjects.get(0).getCredits() / totalCredits;

	                    // Randomly select a subject considering weightage
	                    Subjects selectedSubject = selectSubjectWeighted(subjects, weightage);

	                    // Update credits
	                    selectedSubject.setCredits(selectedSubject.getCredits() - 1);

	                    // Add subject to slot along with teachers' names
	                    StringBuilder subjectWithTeachers = new StringBuilder(selectedSubject.getName() + " (");
	                    String[] subjectTeachers = selectedSubject.getTeachers();
	                    if (subjectTeachers != null) {
	                        for (int k = 0; k < subjectTeachers.length; k++) {
	                            if (k > 0) {
	                                subjectWithTeachers.append(", ");
	                            }
	                            subjectWithTeachers.append(subjectTeachers[k]);
	                        }
	                    } else {
	                        subjectWithTeachers.append("No teachers assigned");
	                    }
	                    subjectWithTeachers.append(")");
	                    classSlots.get(a).get(i).add(subjectWithTeachers.toString());

	                    // Remove subject if all credits are used
	                    if (selectedSubject.getCredits() == 0) {
	                        subjects.remove(selectedSubject);
	                        totalCredits -= selectedSubject.getCredits();
	                    }
	                } 
	            }
	        }
	    }

	    // Print timetable for each class
	    for (int a = 0; a < numofclasses; a++) {
	        System.out.println("Timetable for Class " + (a + 1) + ":");
	        for (int i = 0; i < NUM_DAYS_IN_WEEK; i++) {
	            System.out.println("Day " + (i + 1) + ": " + classSlots.get(a).get(i));
	        }
	        System.out.println();
	        
	        writeTimetableToFile(classSlots.get(a), a + 1);
	        System.out.println("Timetable for Class " + (a + 1) + " written to file.");
	        
	    }
	    
	    displayTeacherSchedule(allSubjects);
	}

	
	private static List<String> loadTimetableFromFile(String filename) {
        List<String> loadedTimetable = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                loadedTimetable.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error loading timetable from file: " + e.getMessage());
        }
        return loadedTimetable;
    }



    private static void viewTimetable() {
        System.out.println("View Timetable option selected");

        // Prompt the user to choose which timetable to view first
        System.out.println("Enter the class number you want to view (1-" + numofclasses + "):");
        int classNumber = getIntInput(1, numofclasses);

        // Construct the filename for the selected class
        String filename = "timetable" + classNumber + ".txt";

        // Load and display the timetable for the selected class
        List<String> classTimetable = loadTimetableFromFile(filename);
        if (!classTimetable.isEmpty()) {
            System.out.println("\nViewing Timetable for Class " + classNumber + ":\n");
            for (String row : classTimetable) {
                System.out.println(row);
            }
        } 
        else {
            System.out.println("No timetable found for Class " + classNumber);
        }
    }


    private static void modifyTimetableFromFile() {
        System.out.println("Modify Timetable option selected");

        // Prompt the user for the class number to modify
        System.out.println("Enter the class number you want to modify:");
        int classNumber = getIntInput(1, numofclasses);

        // Load the timetable for the specified class from the file
        List<String> loadedTimetable = loadTimetableFromFile("timetable" + classNumber + ".txt");

        if (loadedTimetable.isEmpty()) {
            System.out.println("Timetable for Class " + classNumber + " is empty. Please create a timetable first.");
            return;
        }

        // Display the current timetable for user reference
        System.out.println("\nCurrent Timetable for Class " + classNumber + ":");
        for (String row : loadedTimetable) {
            System.out.println(row);
        }

        // Prompt the user for modification
        System.out.println("\nEnter the day you want to modify (e.g., Monday):");
        String dayToModify = scanner.nextLine().trim();
        if (!Arrays.asList(DAYS_OF_WEEK).contains(dayToModify)) {
            System.out.println("Invalid day entered.");
            return;
        }

        System.out.println("Enter the period you want to modify (1-7):");
        int periodToModify = getIntInput(1, 7);

        System.out.println("Enter the new subject name or break:");
        String newEntry = scanner.nextLine().trim();

        // Update the timetable entry
        int dayIndex = Arrays.asList(DAYS_OF_WEEK).indexOf(dayToModify);
        if (dayIndex >= 0 && dayIndex < loadedTimetable.size()) {
            String[] dayEntries = loadedTimetable.get(dayIndex).split(" \\| ");
            if (periodToModify > 0 && periodToModify <= dayEntries.length) {
                dayEntries[periodToModify - 1] = newEntry;
                StringBuilder updatedDayEntry = new StringBuilder(dayEntries[0]);
                for (int i = 1; i < dayEntries.length; i++) {
                    updatedDayEntry.append(" | ").append(dayEntries[i]);
                }
                loadedTimetable.set(dayIndex, updatedDayEntry.toString());

                // Write the updated timetable back to the file
                
             // Wrap loadedTimetable inside another list to represent each day's timetable separately
                List<List<String>> loadedTimetableWrapped = new ArrayList<>();
                loadedTimetableWrapped.add(loadedTimetable);
                writeTimetableToFile(loadedTimetableWrapped, classNumber);

               
                
                System.out.println("Timetable for Class " + classNumber + " modified successfully!");
            } else {
                System.out.println("Invalid period entered.");
            }
        } else {
            System.out.println("Invalid day entered.");
        }
    }

    
    private static void writeTimetableToFile(List<List<String>> timetable, int classNumber) {
        String fileName = getTimetableFileName(classNumber);
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (List<String> dayEntries : timetable) {
                StringBuilder dayEntry = new StringBuilder();
                for (String entry : dayEntries) {
                    if (dayEntry.length() > 0) {
                        dayEntry.append(" | ");
                    }
                    dayEntry.append(entry);
                }
                writer.println(dayEntry.toString());
            }
            System.out.println("Timetable for Class " + classNumber + " written to file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error writing timetable to file: " + e.getMessage());
        }
    }


	private static String getTimetableFileName(int index) {
	    switch (index) {
	        case 1:
	            return TIMETABLE_FILE_1;
	        case 2:
	            return TIMETABLE_FILE_2;
	        case 3:
	            return TIMETABLE_FILE_3;
	        case 4:
	            return TIMETABLE_FILE_4;
	        default:
	            return null; // or throw an exception, depending on your error handling strategy
	    }
	}

	
	
	
    // Helper method to select a subject with weighted probability
    private static Subjects selectSubjectWeighted(List<Subjects> subjects, double weightage) {
        double rand = Math.random();
        double cumulativeProbability = 0.0;
        for (Subjects subject : subjects) {
            cumulativeProbability += (double) subject.getCredits() / subjects.size() / weightage;
            if (rand <= cumulativeProbability) {
                return subject;
            }
        }
        // If for some reason a subject cannot be selected, return the first one
        return subjects.get(0);
    }
    
   
    
    
    private static void displayTeacherSchedule(List<Subjects> subjects) {
        System.out.println("\nTeacher Schedule:\n");

        // Create a map to store the schedule for each teacher on each day
        Map<String, Map<String, List<String>>> teacherSchedule = new HashMap<>();

        // Initialize the map with empty lists for each teacher on each day
        for (Subjects subject : subjects) {
            for (String teacher : subject.getTeachers()) {
                teacherSchedule.putIfAbsent(teacher, new HashMap<>());
                for (String day : DAYS_OF_WEEK) {
                    teacherSchedule.get(teacher).put(day, new ArrayList<>());
                }
            }
        }

        // Populate the schedule for each teacher on each day
        for (Subjects subject : subjects) {
            String subjectName = subject.getName();
            String[] subjectTeachers = subject.getTeachers();
            for (String teacher : subjectTeachers) {
                for (String day : DAYS_OF_WEEK) {
                    // Append the subject name to the teacher's schedule on the corresponding day
                    teacherSchedule.get(teacher).get(day).add(subjectName);
                }
            }
        }

        // Display the schedule for each teacher on each day
        for (String teacher : teacherSchedule.keySet()) {
            System.out.println("Teacher: " + teacher);
            for (String day : DAYS_OF_WEEK) {
                System.out.println(day + " Schedule: " + teacherSchedule.get(teacher).get(day));
            }
            System.out.println(); // Add a blank line for readability
        }
    }
    
    
    private static int getIntInput(int min, int max) {
        int input;
        while (true) {
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (input < min || input > max) {
                    throw new NumberFormatException();
                }
                break;
            } 
            catch (NumberFormatException e) {
            }
        }
        return input;
    }
}

class Subjects{
    private String name;
    private int units;
    private int daysToComplete;
    private int credits; // New attribute for credits
    private String[] teachers; // Array to store teachers for the class

    public Subjects(String name, int units, int daysToComplete, int credits, String[] teachers) {
        this.name = name;
        this.units = units;
        this.daysToComplete = daysToComplete;
        this.credits = credits;
        this.teachers = teachers;
    }

    // Getters and setters for attributes
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public int getDaysToComplete() {
        return daysToComplete;
    }

    public void setDaysToComplete(int daysToComplete) {
        this.daysToComplete = daysToComplete;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String[] getTeachers() {
        return teachers != null ? teachers : new String[0];
    }

    public void setTeachers(String[] teachers) {
        this.teachers = teachers;
    }
}