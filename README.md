# personapp-hexa-spring-boot
Pasos para el despliegue:
1. Instalar MariaDB en puerto 3307.
2. Instalar MongoDB en puerto 27017.
3. Clonar el proyecto en sus archivos por medio del siguiente comando en el git branch: git clone -b develop https://github.com/JuanFelipe14/personapp-hexa-spring-boot-grupo2.git
4. Ejecutar los scripts en las respectivas bases de datos MariaDB y MongoDB para tenerlas conectadas para correr el proyecto
5. Se va a correr el programa dandole click derecho en PersonAppCli en donde va a aparecer un botón que dice 'Run 'PersonAppCli.main()''
6. Van a aparecer los distintos menus para cada entidad en que se va a lograr probar el Cli y en donde se va a poder seleccionar adicionalmente cual base de datos quiere implementar.
7. Posterior a la prueba del Cli, vamos a correr el Rest el cual corre en el puerto 3000 y se debe abrir el swagger para verificar su funcionalidad en http://localhost:3000/swagger-ui.html en donde se va a poder interactuar con las peticiones que estan desplegadas.

