# Mejoras de pedidos y reservas

## Qué puede hacer el cliente

- Añadir productos al carrito y registrar un pedido.
- Elegir **Delivery** o **Recojo en tienda**. Para delivery, la dirección es obligatoria; el costo y la cobertura se coordinan con el local. Para recojo, la dirección y el horario se confirman por teléfono porque el proyecto todavía no tiene una dirección comercial configurada.
- Marcar que desea una llamada para confirmar. La preferencia se guarda con el pedido junto con el teléfono y la dirección.
- Solicitar una reserva indicando nombre, teléfono, fecha, hora, número de personas y un comentario opcional. La solicitud se guarda como **Pendiente de confirmación**; el formulario no promete una mesa hasta que el negocio confirme la disponibilidad.

## Datos guardados

El backend crea la tabla `reservas` automáticamente mediante JPA, igual que las otras entidades del proyecto. La preferencia de llamada del pedido se guarda en la columna `solicitar_llamada`. Las reservas se guardan con fecha de visita, cantidad de personas, estado y fecha de solicitud.

Se puede comprobar el flujo creando una reserva y un pedido en la página; el identificador y el estado aparecen en la confirmación. En MySQL, los registros están en las tablas `reservas` y `pedidos` de la base configurada. En H2, los datos son temporales y se pierden al reiniciar la aplicación.

## Sobre llamadas y mensajes de voz

Esta versión **guarda la solicitud de que el negocio llame**, pero no inicia llamadas, no envía WhatsApp/SMS y no genera un mensaje de voz automático. Para automatizar un aviso como “tu pedido está listo” hace falta conectar un proveedor de telefonía o mensajería, configurar una cuenta y credenciales, tener un número comercial verificado y definir qué estados disparan cada mensaje. El estado actual del pedido lo modifica el personal del local; no se debe mostrar como listo hasta que lo cambie.

Antes de publicar el flujo para clientes reales, el proyecto necesita una pantalla administrativa protegida para revisar y confirmar pedidos y reservas. Los endpoints existentes de pedidos están abiertos en la configuración actual, así que no se deben usar para mostrar datos personales públicamente en producción.
