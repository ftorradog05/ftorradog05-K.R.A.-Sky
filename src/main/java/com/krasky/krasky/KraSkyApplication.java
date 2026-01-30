package com.krasky.krasky;

import com.krasky.krasky.model.Usuario;
import com.krasky.krasky.model.Rol;
import com.krasky.krasky.repository.UsuarioRepository;
import com.krasky.krasky.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class KraSkyApplication {

	public static void main(String[] args) {
		SpringApplication.run(KraSkyApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private RolRepository rolRepository;

	@Bean
	public CommandLineRunner initData() {
		return args -> {
			System.out.println("=================================================");
			System.out.println("🚀 VERIFICANDO DATOS INICIALES DEL SISTEMA 🚀");

			// 1. Asegurar que los ROLES existan
			crearRolSiNoExiste(Rol.RolNombre.ROLE_ADMIN);
			crearRolSiNoExiste(Rol.RolNombre.ROLE_USER);
			crearRolSiNoExiste(Rol.RolNombre.ROLE_EMPLEADO);

			// 2. Asegurar que los USUARIOS existan (Pass: 1234)
			// Si ya existen, NO los toca (así no pierdes cambios futuros)
			crearUsuarioSiNoExiste("admin", "1234", "admin@krasky.com", Rol.RolNombre.ROLE_ADMIN, Rol.RolNombre.ROLE_USER);
			crearUsuarioSiNoExiste("alberto", "1234", "alberto@krasky.com", Rol.RolNombre.ROLE_ADMIN, Rol.RolNombre.ROLE_USER);
			crearUsuarioSiNoExiste("empleado", "1234", "empleado@krasky.com", Rol.RolNombre.ROLE_EMPLEADO);
			crearUsuarioSiNoExiste("usuario", "1234", "usuario@krasky.com", Rol.RolNombre.ROLE_USER);

			System.out.println("=================================================");
		};
	}

	private void crearRolSiNoExiste(Rol.RolNombre nombre) {
		if (rolRepository.findByNombre(nombre).isEmpty()) {
			Rol rol = new Rol();
			rol.setNombre(nombre);
			rolRepository.save(rol);
			System.out.println(" -> Rol creado: " + nombre);
		}
	}

	private void crearUsuarioSiNoExiste(String username, String rawPass, String email, Rol.RolNombre... roles) {
		// Si el usuario ya existe, no hacemos nada para respetar cambios manuales
		if (usuarioRepository.findByUsername(username).isPresent()) {
			System.out.println("ℹ️  Usuario '" + username + "' ya existe. Omitiendo creación.");
			return;
		}

		// Si no existe, lo creamos
		Usuario usuario = new Usuario();
		usuario.setUsername(username);
		usuario.setEmail(email);
		usuario.setPassword(passwordEncoder.encode(rawPass)); // Encriptamos aquí mismo
		usuario.setEnabled(true);
		usuario.setNombre(username.substring(0, 1).toUpperCase() + username.substring(1)); // Capitalizar nombre
		usuario.setApellidos("System Account");

		// Asignar roles
		Set<Rol> rolesSet = new HashSet<>();
		for (Rol.RolNombre nombreRol : roles) {
			rolRepository.findByNombre(nombreRol).ifPresent(rolesSet::add);
		}
		usuario.setRoles(rolesSet);

		usuarioRepository.save(usuario);
		System.out.println("✅ Usuario CREADO: " + username + " (Pass: " + rawPass + ")");
	}
}