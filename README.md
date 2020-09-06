# taxi-app
Simple taxi booking application

Technologies Used
-- Java 8
-- Spring Boot
-- Maven
-- HTML

Implementation and Assumptions: We own a Fueber taxi service and our office is located at the coordinates (12.976143,77.570541) in Bangalore. 
                                We provide services only around 25 kilometers from our the former mentioned coordinates with only 10 taxi (5: White and 5: Pink)
                                The kilometer limit shall be increased

How to build and run the applications
1. Eclipse IDE: Import the project into eclipse and run the project as maven build with the command "package".
                After the successful build, navigate to target directory and run the jar using "java -jar fueber-taxi.jar". The application would start at port 8080
				
2. Start.sh: Run this script which will do maven package and start the application at localhost at port 8080.

API Documenttation: Once the application is succesfully started, type the URL "http://localhost:8080/swagger-ui.html" and refer the API documentation.

HTML Page to View the Available Taxi: In the static folder of the project, there is a TaxiList.html. Kindly run this HTML to view the list of available taxi.

Validations: 
1. Customer pickup and drop coordinates are validated whether the location lies within in the 25 kilometer limit.
2. Customer booking is validated based on the mobile number whether already an active ride is available for the customer
3. The customer request is rejected in case the request taxi is not available.
4. The ride status change (STARTED, CANCELED and COMPLETED) are validated accordingly.
