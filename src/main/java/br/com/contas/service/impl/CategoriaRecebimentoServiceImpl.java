package br.com.contas.service.impl;

import br.com.contas.domain.CategoriaRecebimento;
import br.com.contas.repository.CategoriaRecebimentoRepository;
import br.com.contas.service.CategoriaRecebimentoService;
import br.com.contas.service.dto.CategoriaRecebimentoDTO;
import br.com.contas.service.mapper.CategoriaRecebimentoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CategoriaRecebimento}.
 */
@Service
@Transactional
public class CategoriaRecebimentoServiceImpl implements CategoriaRecebimentoService {

    private final Logger log = LoggerFactory.getLogger(CategoriaRecebimentoServiceImpl.class);

    private final CategoriaRecebimentoRepository categoriaRecebimentoRepository;

    private final CategoriaRecebimentoMapper categoriaRecebimentoMapper;

    public CategoriaRecebimentoServiceImpl(
        CategoriaRecebimentoRepository categoriaRecebimentoRepository,
        CategoriaRecebimentoMapper categoriaRecebimentoMapper
    ) {
        this.categoriaRecebimentoRepository = categoriaRecebimentoRepository;
        this.categoriaRecebimentoMapper = categoriaRecebimentoMapper;
    }

    @Override
    public CategoriaRecebimentoDTO save(CategoriaRecebimentoDTO categoriaRecebimentoDTO) {
        log.debug("Request to save CategoriaRecebimento : {}", categoriaRecebimentoDTO);
        CategoriaRecebimento categoriaRecebimento = categoriaRecebimentoMapper.toEntity(categoriaRecebimentoDTO);
        categoriaRecebimento = categoriaRecebimentoRepository.save(categoriaRecebimento);
        return categoriaRecebimentoMapper.toDto(categoriaRecebimento);
    }

    @Override
    public CategoriaRecebimentoDTO update(CategoriaRecebimentoDTO categoriaRecebimentoDTO) {
        log.debug("Request to update CategoriaRecebimento : {}", categoriaRecebimentoDTO);
        CategoriaRecebimento categoriaRecebimento = categoriaRecebimentoMapper.toEntity(categoriaRecebimentoDTO);
        categoriaRecebimento = categoriaRecebimentoRepository.save(categoriaRecebimento);
        return categoriaRecebimentoMapper.toDto(categoriaRecebimento);
    }

    @Override
    public Optional<CategoriaRecebimentoDTO> partialUpdate(CategoriaRecebimentoDTO categoriaRecebimentoDTO) {
        log.debug("Request to partially update CategoriaRecebimento : {}", categoriaRecebimentoDTO);

        return categoriaRecebimentoRepository
            .findById(categoriaRecebimentoDTO.getId())
            .map(existingCategoriaRecebimento -> {
                categoriaRecebimentoMapper.partialUpdate(existingCategoriaRecebimento, categoriaRecebimentoDTO);

                return existingCategoriaRecebimento;
            })
            .map(categoriaRecebimentoRepository::save)
            .map(categoriaRecebimentoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaRecebimentoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CategoriaRecebimentos");
        return categoriaRecebimentoRepository.findAll(pageable).map(categoriaRecebimentoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoriaRecebimentoDTO> findOne(Long id) {
        log.debug("Request to get CategoriaRecebimento : {}", id);
        return categoriaRecebimentoRepository.findById(id).map(categoriaRecebimentoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CategoriaRecebimento : {}", id);
        categoriaRecebimentoRepository.deleteById(id);
    }
}
