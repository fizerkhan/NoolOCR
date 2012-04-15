NooL OCR Framework
------------------

Nool OCR is licensed under GPL V3.
Nool OCR is a framework to integrate all OCR software in one platfrom.

Overview:
---------
Optical Character Recognition (OCR) is a process of converting printed 
materials into text or word processing files that can be easily edited and stored. 
The technology has enabled such materials to be stored using much less storage 
space than the hard materials

Now a day there are lot of OCR available in the market for different languages
but there is no centralized framework for all languages.The intension of the project
is to create a framework capable to handle all available languages. This can be achieved
through Eclipse plug-in architecture. Internally, we use great tool like Tesseract and 
GOCR. NoolOCR provided a eclipse extension machanism, we can incorporate any tools in future.

Nool OCR is Eclipse RCP product.

Features:
---------

1. Support to convert images into text files.
2. Support to load and convert many images at a time.
3. Support for various OCR application such as GOCR, Tesseract, and Tamil OCR.
4. Support to generate PDF file from text file.

Run Nool OCR
------------

You need Eclipse(Eclipse 3.6 or above is required) to work with Nool OCR.
You have to copy the folders such as 'utils' and 'plugins' folder avilable in 'dependencies' folder
to your Eclipse folder.

You have to import Nool OCR source code into Eclipse. Run ocr.product file under 'edu.panimalar.ocr' plugin.


Steps for execution:
--------------------

1.Open Nool OCR Framework and click new ocr project and give project name,select the language and click next button.

2.Add images from the  external folder and click finish button.

3.Select the project and click read images icon.New window will opened in that window select ocr reader and click ok.

4.Images will be read by ocr reader.select the project and click generate document icon then the output will be converted into pdf format.

5.Similar way you can generate more no of project in that nool ocr framework.


Known Issues:
-------------
1. Not able to arrange the text file while generating the PDF file.

