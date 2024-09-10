# Guía para Ejecutar los Tests

## Pasos para Configuración y Ejecución

1. **Crear la Carpeta de Configuración**
   - Crea la carpeta `/cypress/environment`.

2. **Configurar el Archivo de Entorno**
   - Dentro de la carpeta `/cypress/environment`, crea un archivo llamado `environment.js`.
   - En el archivo `environment.js`, agrega la siguiente línea de código:
     ```javascript
     export const WEB_URL = "http://HOST";
     ```
     Reemplaza `HOST` con la dirección del servidor de pruebas.

3. **Instalar las Dependencias**
   - Ejecuta el siguiente comando para instalar las dependencias necesarias:
     ```bash
     npm install
     ```

4. **Ejecutar los Tests**
   - Usa el siguiente comando para abrir Cypress y ejecutar los tests:
     ```bash
     npx cypress open
     ```

Sigue estos pasos en orden para configurar tu entorno y ejecutar los tests de manera efectiva.
