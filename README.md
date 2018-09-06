# RetrofitForServer_client1

## RetrofitForServer_client1 is a client part of microservice application using Retrofit.

This client allows user to upload image to server and to download it back with setting unique name to it. Downloaded file saves to root directory of phone memory.

Client uses following url address - https://spring-boot-server1.herokuapp.com for connecting with server and sending requests.

On the main screen user can see some buttons: button "Pick image" in the bottom, buttons "Upload" and "Download" next to each other at the top of screen.

![alt](https://image.ibb.co/hHLBCz/Screenshot_2018_09_06_15_38_10_239_com_example_retrofitforserver_client1_2.png)

Also there is a empty space in the center of screen - here picked image will be shown.

![alt](https://image.ibb.co/jZzGee/Screenshot_2018_09_06_15_39_12_174_com_example_retrofitforserver_client1_2.png)

After picking image user can upload it to server by tapping on the "Upload" button. In case of successfull uploading appropriate pop-up message will be shown in the bottom of screen. If uploading failed user will see message about it.

![alt](https://image.ibb.co/d0hrCz/Screenshot_2018_09_06_15_39_22_243_com_example_retrofitforserver_client1_2.png)

When image is successfully uploaded user can download it to his phone by tapping on the "Download" button. Programm will place it in the root of memory and show appropriate pop-up message in case of successfull download. And error-message will be shown if proccess failed.

![alt](https://image.ibb.co/iBeGee/Screenshot_2018_09_06_15_40_17_385_com_ghisler_android_Total_Commander_1.jpg)
