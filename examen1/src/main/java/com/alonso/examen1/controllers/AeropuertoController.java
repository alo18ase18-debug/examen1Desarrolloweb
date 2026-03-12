package com.alonso.examen1.controllers;

import com.alonso.examen1.model.Aeropuerto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador CRUD para la entidad Aeropuerto.
 *
 * Rutas disponibles:
 *   GET  /                            → index (página de inicio)
 *   GET  /aeropuertos                 → lista todos los aeropuertos
 *   GET  /aeropuertos/nuevo           → formulario para crear
 *   POST /aeropuertos/guardar         → guarda un nuevo aeropuerto
 *   GET  /aeropuertos/editar/{id}     → formulario de edición pre-cargado
 *   POST /aeropuertos/actualizar      → guarda los cambios del aeropuerto editado
 *   GET  /aeropuertos/eliminar/{id}   → página de confirmación de eliminación
 *   POST /aeropuertos/borrar/{id}     → ejecuta la eliminación
 */
@Controller
public class AeropuertoController {

    // DataSource inyectado automáticamente por Spring Boot
    // según la configuración en application.properties
    @Autowired
    private DataSource dataSource;

    /* ══════════════════════════════════════════════
       ÍNDICE — GET /
       ══════════════════════════════════════════════ */
    @GetMapping("/")
    public String index() {
        return "index";     // templates/index.html
    }

    /* ══════════════════════════════════════════════
       LISTAR — GET /aeropuertos
       ══════════════════════════════════════════════ */
    @GetMapping("/aeropuertos")
    public String listar(Model model) {
        List<Aeropuerto> lista = new ArrayList<>();

        String sql = "SELECT id, nombre, ciudad, pais, codigo FROM aeropuertos ORDER BY id";

        try (Connection conn = dataSource.getConnection();
             Statement  stmt = conn.createStatement();
             ResultSet  rs   = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Aeropuerto a = new Aeropuerto(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("ciudad"),
                    rs.getString("pais"),
                    rs.getString("codigo")
                );
                lista.add(a);
            }

        } catch (SQLException e) {
            model.addAttribute("error", "Error al obtener los aeropuertos: " + e.getMessage());
        }

        model.addAttribute("aeropuertos", lista);
        return "lista";     // templates/lista.html
    }

    /* ══════════════════════════════════════════════
       FORMULARIO CREAR — GET /aeropuertos/nuevo
       ══════════════════════════════════════════════ */
    @GetMapping("/aeropuertos/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("aeropuerto", new Aeropuerto());
        return "crear";     // templates/crear.html
    }

    /* ══════════════════════════════════════════════
       GUARDAR NUEVO — POST /aeropuertos/guardar
       ══════════════════════════════════════════════ */
    @PostMapping("/aeropuertos/guardar")
    public String guardar(@ModelAttribute Aeropuerto aeropuerto,
                          RedirectAttributes redirectAttrs) {

        String sql = "INSERT INTO aeropuertos (id, nombre, ciudad, pais, codigo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt   (1, aeropuerto.getId());
            ps.setString(2, aeropuerto.getNombre());
            ps.setString(3, aeropuerto.getCiudad());
            ps.setString(4, aeropuerto.getPais());
            ps.setString(5, aeropuerto.getCodigo());
            ps.executeUpdate();

            redirectAttrs.addFlashAttribute("mensaje",
                "Aeropuerto '" + aeropuerto.getNombre() + "' creado exitosamente.");

        } catch (SQLException e) {
            redirectAttrs.addFlashAttribute("error",
                "Error al guardar: " + e.getMessage());
            return "redirect:/aeropuertos/nuevo";
        }

        return "redirect:/aeropuertos";
    }

    /* ══════════════════════════════════════════════
       FORMULARIO EDITAR — GET /aeropuertos/editar/{id}
       ══════════════════════════════════════════════ */
    @GetMapping("/aeropuertos/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model) {

        String sql = "SELECT id, nombre, ciudad, pais, codigo FROM aeropuertos WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Aeropuerto a = new Aeropuerto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("ciudad"),
                        rs.getString("pais"),
                        rs.getString("codigo")
                    );
                    model.addAttribute("aeropuerto", a);
                    return "editar";    // templates/editar.html
                } else {
                    model.addAttribute("error", "No se encontró el aeropuerto con ID " + id);
                    return "redirect:/aeropuertos";
                }
            }

        } catch (SQLException e) {
            model.addAttribute("error", "Error al buscar el aeropuerto: " + e.getMessage());
            return "redirect:/aeropuertos";
        }
    }

    /* ══════════════════════════════════════════════
       ACTUALIZAR — POST /aeropuertos/actualizar
       ══════════════════════════════════════════════ */
    @PostMapping("/aeropuertos/actualizar")
    public String actualizar(@ModelAttribute Aeropuerto aeropuerto,
                             RedirectAttributes redirectAttrs) {

        String sql = "UPDATE aeropuertos SET nombre=?, ciudad=?, pais=?, codigo=? WHERE id=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, aeropuerto.getNombre());
            ps.setString(2, aeropuerto.getCiudad());
            ps.setString(3, aeropuerto.getPais());
            ps.setString(4, aeropuerto.getCodigo());
            ps.setInt   (5, aeropuerto.getId());
            ps.executeUpdate();

            redirectAttrs.addFlashAttribute("mensaje",
                "Aeropuerto actualizado exitosamente.");

        } catch (SQLException e) {
            redirectAttrs.addFlashAttribute("error",
                "Error al actualizar: " + e.getMessage());
        }

        return "redirect:/aeropuertos";
    }

    /* ══════════════════════════════════════════════
       CONFIRMAR ELIMINAR — GET /aeropuertos/eliminar/{id}
       ══════════════════════════════════════════════ */
    @GetMapping("/aeropuertos/eliminar/{id}")
    public String mostrarConfirmacionEliminar(@PathVariable int id, Model model) {

        String sql = "SELECT id, nombre, ciudad, pais, codigo FROM aeropuertos WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Aeropuerto a = new Aeropuerto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("ciudad"),
                        rs.getString("pais"),
                        rs.getString("codigo")
                    );
                    model.addAttribute("aeropuerto", a);
                    return "eliminar";  // templates/eliminar.html
                } else {
                    model.addAttribute("error", "No se encontró el aeropuerto con ID " + id);
                }
            }

        } catch (SQLException e) {
            model.addAttribute("error", "Error al buscar el aeropuerto: " + e.getMessage());
        }

        return "redirect:/aeropuertos";
    }

    /* ══════════════════════════════════════════════
       EJECUTAR ELIMINACIÓN — POST /aeropuertos/borrar/{id}
       ══════════════════════════════════════════════ */
    @PostMapping("/aeropuertos/borrar/{id}")
    public String eliminar(@PathVariable int id, RedirectAttributes redirectAttrs) {

        String sql = "DELETE FROM aeropuertos WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                redirectAttrs.addFlashAttribute("mensaje",
                    "Aeropuerto eliminado exitosamente.");
            } else {
                redirectAttrs.addFlashAttribute("error",
                    "No se encontró el aeropuerto con ID " + id);
            }

        } catch (SQLException e) {
            redirectAttrs.addFlashAttribute("error",
                "Error al eliminar: " + e.getMessage());
        }

        return "redirect:/aeropuertos";
    }
}
