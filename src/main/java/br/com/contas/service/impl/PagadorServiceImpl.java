package br.com.contas.service.impl;

import br.com.contas.domain.Pagador;
import br.com.contas.repository.PagadorRepository;
import br.com.contas.service.PagadorService;
import br.com.contas.service.dto.PagadorDTO;
import br.com.contas.service.mapper.PagadorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pagador}.
 */
@Service
@Transactional
public class PagadorServiceImpl implements PagadorService {

    private final Logger log = LoggerFactory.getLogger(PagadorServiceImpl.class);

    private final PagadorRepository pagadorRepository;

    private final PagadorMapper pagadorMapper;

    public PagadorServiceImpl(PagadorRepository pagadorRepository, PagadorMapper pagadorMapper) {
        this.pagadorRepository = pagadorRepository;
        this.pagadorMapper = pagadorMapper;
    }

    @Override
    public PagadorDTO save(PagadorDTO pagadorDTO) {
        log.debug("Request to save Pagador : {}", pagadorDTO);
        Pagador pagador = pagadorMapper.toEntity(pagadorDTO);
        pagador = pagadorRepository.save(pagador);
        return pagadorMapper.toDto(pagador);
    }

    @Override
    public PagadorDTO update(PagadorDTO pagadorDTO) {
        log.debug("Request to update Pagador : {}", pagadorDTO);
        Pagador pagador = pagadorMapper.toEntity(pagadorDTO);
        pagador = pagadorRepository.save(pagador);
        return pagadorMapper.toDto(pagador);
    }

    @Override
    public Optional<PagadorDTO> partialUpdate(PagadorDTO pagadorDTO) {
        log.debug("Request to partially update Pagador : {}", pagadorDTO);

        return pagadorRepository
            .findById(pagadorDTO.getId())
            .map(existingPagador -> {
                pagadorMapper.partialUpdate(existingPagador, pagadorDTO);

                return existingPagador;
            })
            .map(pagadorRepository::save)
            .map(pagadorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PagadorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pagadors");
        return pagadorRepository.findAll(pageable).map(pagadorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PagadorDTO> findOne(Long id) {
        log.debug("Request to get Pagador : {}", id);
        return pagadorRepository.findById(id).map(pagadorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pagador : {}", id);
        pagadorRepository.deleteById(id);
    }
}
