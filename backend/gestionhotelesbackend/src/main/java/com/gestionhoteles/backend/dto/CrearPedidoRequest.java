package com.gestionhoteles.backend.dto;
import jakarta.validation.Valid; import jakarta.validation.constraints.*; import java.util.List;
public record CrearPedidoRequest(@NotBlank String nombreCliente,@NotBlank String telefono,@Email String email,String direccion,@NotBlank String tipoEntrega,@NotEmpty List<@Valid ItemRequest> items) { public record ItemRequest(@NotNull @Positive Integer idProducto,@NotNull @Positive Integer cantidad){} }
