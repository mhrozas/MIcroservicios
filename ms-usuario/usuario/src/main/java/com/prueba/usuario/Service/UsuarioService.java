package com.prueba.usuario.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.usuario.Model.Usuario;
import com.prueba.usuario.Repository.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    public Usuario save(Usuario usuario) {
        return repository.save(usuario);
    }

    public List<Usuario> findAll() {
        return repository.findAll();
    }

    public Map<String, Object> buscarUsuarioCompleto(Integer id) {
        Usuario usuario = repository.findById(id).orElse(null);
        Map<String, Object> respuesta = new HashMap<>();
        if (usuario != null) {
            respuesta.put("id", usuario.getId());
            respuesta.put("nombre", usuario.getNombre());
            respuesta.put("email", usuario.getEmail());
        }
        return respuesta;
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public Usuario update(Integer id, Usuario usuario) {
    repository.findById(id).orElseThrow(() -> {
        return new RuntimeException("Usuario no encontrado");
    });
    usuario.setId(id);
    return repository.save(usuario);
    }

    public Map<String, Object> buscarPorEmail(String email) {
        List<Usuario> usuarios = repository.findByEmail(email);
        Map<String, Object> res = new HashMap<>();
        if (usuarios == null || usuarios.isEmpty()) {
            return res;
        }
        Usuario usuario = usuarios.get(0);
        res.put("id", usuario.getId());
        res.put("nombre", usuario.getNombre());
        res.put("email", usuario.getEmail());
        res.put("telefono", usuario.getTelefono());
        return res;
    }


}
