## 3.1 UNWDMIDP

The United Nation Weather Data Management Institute Data Processor (UNWDMIDP) is the application used to gather the weatherdata as measured by the 8000 weatherstations available to Da Vinci Data. The Data Processor collects the measurements of each weatherstation every second. It checks if any data is missing or malformed and repairs those measurements by looking at the trend of previous measurements. It stores the raw measurements in a Mongo database which is then reduced to the actual working dataset as detailed in the Map Reduce section of this report.

The Data Processor was developed to be as lightweight as possible as to handle the 8000 measurements per second.