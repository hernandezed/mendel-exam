
# Challenge técnico de Eduardo Hernandez para Mendel  
  
 ## Como levantar la aplicación con docker
El proyecto contiene un Dockerfile (creando la imagen para iniciar el proyecto) y un compose (para simplificar el levantarlo). 
Se debe contar con la variable de entorno APPLICATION_PORT para poder ejecutar el buildeo y el starteo del contenedor. Igualmente, se pueden proveer directamente por linea de comandos:
```
	APPLICATION_PORT=8080 docker-compose build
	APPLICATION_PORT=8080 docker-compose up
```

## Swagger  
La aplicación disponibiliza (mediante Spring docs) una vista para acceder a la documentación de swagger generada.
[Swagger](http://localhost:{APPLICATION_PORT}/swagger-ui.html)

## Representación de las transacciones en la capa de persistencia
Se decidió hacer uso de un HashMap para persistir las transacciones con la relación transaction-id -> transaction para simplificar su búsqueda en lo que a complejidad temporal se refiere (O(1)).
La misma decisión se tomo respecto a los tipos de transacciones, guardando la relacion type -> transaction-ids.
Por otro lado, para representar una transaccion, se decidio utilizar el patron de diseño Composite, como se ve en el siguiente diagrama:
![transactionNode](https://github.com/hernandezed/mendel-exam/blob/master/docs/transactionNode.png)

## Arquitectura
Se eligió partir por una arquitectura limpia. Con esto busco desacoplar completamente la capa de negocio de la capa de acceso a datos y presentación (permitiendo por ejemplo, reemplazar la implementación del  repositorio por una que acceda a una base de datos sin necesidad de modificar la lógica de negocio). 
El concepto de puerto se crea para romper el acoplamiento que existiria, en caso de no existir, entre la capa de negocio y la de acceso a datos).
Por otro lado los casos de uso, nacen de implementar los principios de Responsabilidad única y segregación de interfaces. La implementación de casos de uso están hechas en Java vanilla (permitiendo su uso junto con cualquier framework, mientras se provean las interfaces necesarias). Ademas, su forma esta inspirada en el patron de diseño Command.
Por ultimo, la existencia de los BOs tiene dos motivos:
1. Encapsular las reglas de negocio
2. Servir de interfaz para acceder a los datos recibidos via API o obtenidos desde la base de datos.


 
