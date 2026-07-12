UNIVERSIDAD NUR
Carrera de Ingeniería de Sistemas
INFORME DE DESARROLLO DE PROYECTO
Materia: Programación I
Nombre del proyecto:	Gestor de Imágenes (App Manejo de Imágenes)
Integrante(s):	[Escriba aquí su nombre completo]
Docente:	[Nombre del/la docente]
Grupo:	[Indique paralelo o grupo]
Fecha de entrega:	[dd/mm/aaaa]

Índice
1. Introducción
2. Objetivo del proyecto
3. Desarrollo del proyecto
   3.1 Tecnología utilizada
   3.2 Características básicas del programa
   3.3 Otros aspectos del desarrollo
4. Conclusiones
5. Anexos

1. Introducción

El presente informe describe el desarrollo de un programa de escritorio llamado "Gestor de Imágenes", una aplicación construida en Java que permite cargar imágenes, editarlas mediante herramientas de dibujo y pintado, y almacenarlas de forma persistente en una base de datos. El proyecto surge como ejercicio integrador de la materia, con el propósito de aplicar de manera práctica conceptos de programación orientada a objetos, interfaces gráficas, estructuras de datos propias y conexión a bases de datos.

La necesidad que atiende el programa es la de contar con una herramienta sencilla que no solo permita modificar imágenes a nivel de píxeles (dibujar puntos, líneas, cuadrados y rellenar áreas de color), sino que además guarde tanto las imágenes originales como sus versiones modificadas, asociándolas a un autor protegido por contraseña y a una categoría o etiqueta. De esta forma, cada usuario puede recuperar y editar únicamente las imágenes que le pertenecen.

A lo largo de este informe el lector encontrará los objetivos planteados, la tecnología empleada, una descripción de las funcionalidades y de la estructura interna del código (clases y patrones de diseño utilizados), así como las dificultades encontradas durante el desarrollo, las pruebas realizadas y las conclusiones obtenidas. Los detalles técnicos se abordan en la sección de Desarrollo.

2. Objetivo del proyecto

2.1 Objetivo general

Desarrollar una aplicación de escritorio en Java que permita cargar, editar a nivel de píxeles y almacenar imágenes en una base de datos, gestionando su autoría mediante autenticación por contraseña.

2.2 Objetivos específicos

●	Implementar una interfaz gráfica de usuario con Java Swing que ofrezca menús para importar, modificar, guardar y eliminar imágenes.
●	Desarrollar herramientas de edición sobre la imagen (dibujar punto, línea, cuadrado y rellenar una sección de color mediante flood fill).
●	Aplicar el patrón de diseño Command junto con una estructura de pila (Stack) propia para registrar el historial de acciones y permitir deshacer operaciones.
●	Integrar la persistencia de imágenes, autores y etiquetas en una base de datos PostgreSQL mediante JDBC.
●	Validar los datos ingresados por el usuario (nombres, contraseñas y opciones de menú) controlando errores y campos vacíos.

3. Desarrollo del proyecto

3.1 Tecnología utilizada (lenguaje de programación)

Lenguaje de programación:	Java
Versión:	Java (JDK) — [indique la versión instalada, ej: 17]
IDE / Entorno:	IntelliJ IDEA
Librerías / herramientas:	javax.swing y java.awt (interfaz gráfica y manejo de gráficos), javax.imageio (ImageIO) para lectura/escritura de imágenes, JDBC de PostgreSQL (postgresql 42.6.0) para la base de datos, Apache Log4j 2.23.1 para el registro de eventos, JUnit para pruebas, y Apache Maven como gestor de dependencias y construcción del proyecto.

Se eligió Java como lenguaje principal porque permite trabajar de forma robusta con programación orientada a objetos, ofrece bibliotecas nativas maduras para construir interfaces gráficas de escritorio (Swing) y para el manejo de imágenes (BufferedImage e ImageIO), y cuenta con un excelente soporte para la conexión a bases de datos mediante JDBC. Además, el uso de Maven facilitó la gestión de las dependencias externas (PostgreSQL, Log4j, JUnit) y la portabilidad del proyecto, lo que lo convirtió en la opción más adecuada para integrar edición de imágenes, patrones de diseño y persistencia en una sola aplicación.

3.2 Características básicas del programa

El programa se abre en una ventana principal (PrincipalFrame) que contiene una barra de menús con las opciones "File" y "Options", además de un panel central de edición y un panel secundario de herramientas. Desde el punto de vista del usuario, el flujo típico es el siguiente: se importa una imagen desde el disco, se edita usando las herramientas de dibujo y se guarda en la base de datos asociándola a un autor y a una categoría. Posteriormente, el usuario puede volver a cargar sus imágenes (verificando su contraseña), modificarlas nuevamente o eliminarlas.

Funcionalidades principales:

●	Importar imagen: mediante un selector de archivos (JFileChooser) el usuario elige una imagen de su equipo, que se convierte en una matriz de píxeles para poder editarla.
●	Herramientas de edición: dibujar un punto, dibujar una línea, dibujar un cuadrado y rellenar una sección de color mediante el algoritmo de "flood fill" (relleno por difusión con margen de tolerancia de color).
●	Deshacer acciones (undo): cada operación de dibujo se guarda en un historial, permitiendo revertir la última acción realizada.
●	Guardar imagen: almacena la imagen (original o modificada) en la base de datos, solicitando el nombre del autor, la categoría/etiqueta y el nombre y descripción de la imagen.
●	Cargar y modificar imagen: recupera una imagen desde la base de datos validando la contraseña del autor antes de permitir su edición.
●	Eliminar imagen: borra una imagen de la base de datos previa verificación del autor y su contraseña.

Estructura del código (principales clases y módulos):

●	App: clase principal que inicia la aplicación creando la ventana PrincipalFrame.
●	PrincipalFrame: ventana principal (JFrame); construye los menús y coordina las acciones de importar, guardar, modificar y eliminar imágenes.
●	PrincipalPanel y SecundaryPanel: paneles de la interfaz; el principal muestra y edita la imagen, y el secundario contiene las herramientas de dibujo.
●	ImagePixels: modelo de la imagen; almacena los píxeles en una matriz (int[][]) y el BufferedImage asociado, y notifica cambios mediante el patrón Observer (PropertyChangeSupport).
●	Paquete operations (DrawPoint, DrawLine, DrawSquare, PaintSection): contiene la lógica de cada operación de edición sobre la matriz de píxeles, incluyendo el algoritmo de relleno flood fill.
●	Paquete command (CommandExcecute, DrawPointCommand, DrawLineCommand, DrawSquareCommand, PaintSectionCommand, Invoker): implementa el patrón de diseño Command, encapsulando cada acción como un objeto con métodos execute() y undo().
●	Paquete linkedlist (Node, Stack): estructura de datos de pila implementada manualmente con nodos enlazados, usada como historial para la función de deshacer.
●	Paquete postgres (PostgresConection, SaveImage, LoadImage, DeleteImage y las clases de datos Author, AuthorPostgres, TagImage): gestiona la conexión y las operaciones CRUD contra la base de datos PostgreSQL (guardar, cargar, eliminar imágenes y validar autores).
●	ExcepcionImagen: excepción personalizada para el manejo de errores relacionados con las imágenes.

3.3 Otros aspectos del desarrollo

●	Metodología de trabajo: proyecto desarrollado de forma individual. El trabajo se organizó de manera modular, separando el código en paquetes según su responsabilidad (interfaz gráfica, operaciones, comandos, estructura de datos y persistencia), lo que facilitó avanzar por partes y mantener el código ordenado.
●	Dificultades encontradas y solución: uno de los principales retos fue implementar el algoritmo de relleno por difusión (flood fill) de forma recursiva controlando los límites de la matriz y un margen de tolerancia de color, lo que se resolvió comparando los componentes RGB de cada píxel. Otro desafío fue coordinar el patrón Command con una pila propia para lograr la función de deshacer, así como manejar la conversión entre imágenes (BufferedImage) y arreglos de bytes para almacenarlas en la base de datos.
●	Pruebas realizadas: se realizaron pruebas manuales cargando imágenes de distintos formatos, aplicando cada herramienta de dibujo y verificando la función de deshacer. También se probaron los flujos de guardar, cargar, modificar y eliminar imágenes contra la base de datos, validando el control de errores ante nombres vacíos, contraseñas incorrectas o imágenes inexistentes. El proyecto incluye la estructura de pruebas con JUnit.
●	Control de versiones u otras herramientas: se utilizó Git y GitHub para el control de versiones del código fuente, y Apache Maven para la gestión de dependencias. Se empleó Log4j para el registro de eventos y errores de la aplicación.

4. Conclusiones

El desarrollo de este proyecto permitió reforzar de manera integral conceptos clave de la programación orientada a objetos, tales como el encapsulamiento, la herencia y el uso de interfaces, además de aplicar patrones de diseño reales como Command (para encapsular acciones y deshacerlas) y Observer (para notificar cambios en la imagen). También se puso en práctica la creación de una estructura de datos propia (una pila basada en nodos enlazados) y la manipulación de imágenes a nivel de píxeles, lo que ayudó a comprender mejor cómo funcionan internamente las herramientas de edición gráfica.

Asimismo, la integración con una base de datos PostgreSQL mediante JDBC permitió afianzar conocimientos sobre persistencia de datos y validación de usuarios. Entre los retos superados destacan la implementación del algoritmo de flood fill y la conversión de imágenes a bytes para su almacenamiento. En conjunto, el programa cumplió con el objetivo planteado: ofrecer una aplicación funcional capaz de cargar, editar y almacenar imágenes de forma segura, dejando como aprendizaje la importancia de organizar el código en módulos y de manejar adecuadamente los errores.

5. Anexos

5.1 Capturas de pantalla del programa en ejecución
Guía: Inserte aquí las imágenes (Insertar > Imágenes en Word), mostrando por ejemplo: la ventana principal con la imagen cargada, el uso de una herramienta de dibujo, y el diálogo de guardado con autor y categoría.

[Inserte aquí sus capturas de pantalla]

5.2 Enlace al código fuente
https://github.com/[usuario]/[nombre-del-repositorio]
