Objetivo del app:
El objetivo principal de la aplicación de inventarios para emprendedores es proporcionar una herramienta fácil de usar y eficiente que permita a los usuarios gestionar sus inventarios de manera efectiva. La aplicación busca optimizar el seguimiento de productos, la gestión de existencias y la generación de informes relacionados con el inventario, facilitando así la toma de decisiones y mejorando la eficiencia operativa de los emprendedores.

Descripción del logo y lo que representa: 
Esta version es una caja fuerte como lo dice el nombre Popurri Vault, dando a entender que puedes ocuparla para lo que quieras.


Justificación de la elección del tipo de dispositivo, versión del sistema operativo y las orientaciones soportadas:
La aplicación estará disponible inicialmente para dispositivos móviles, como smartphones y tablets, ya que estos son dispositivos ampliamente utilizados por emprendedores para gestionar sus negocios sobre la marcha. Se enfocará en las versiones más recientes de los sistemas operativos iOS y Android para aprovechar las últimas características y mejoras de seguridad. Las orientaciones admitidas serán tanto vertical como horizontal para adaptarse a diversas preferencias de los usuarios y situaciones de uso.

Credenciales para poder acceder al app (si se requieren):
No se requiere en esta version

Dependencias del proyecto (paquetes y/o frameworks utilizados):
implementation 'androidx.navigation:navigation-fragment-ktx:2.7.1'
    implementation 'androidx.navigation:navigation-ui-ktx:2.7.1'
    implementation 'androidx.preference:preference:1.2.0'
    def room_version = "2.5.2"



    implementation 'androidx.core:core-ktx:1.7.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.9.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

    //Room
    implementation "androidx.room:room-ktx:$room_version"
    kapt "androidx.room:room-compiler:$room_version"


    //Corrutinas con alcance lifecycle
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.6.1"
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1'
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.6.1'
    implementation 'androidx.lifecycle:lifecycle-process:2.6.1'

    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
