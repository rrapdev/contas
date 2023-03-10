package br.com.contas.service.impl;

import br.com.contas.domain.Beneficiario;
import br.com.contas.repository.BeneficiarioRepository;
import br.com.contas.service.BeneficiarioService;
import br.com.contas.service.dto.BeneficiarioDTO;
import br.com.contas.service.mapper.BeneficiarioMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Beneficiario}.
 */
@Service
@Transactional
public class BeneficiarioServiceImpl implements BeneficiarioService {

    private final Logger log = LoggerFactory.getLogger(BeneficiarioServiceImpl.class);

    private final BeneficiarioRepository beneficiarioRepository;

    private final BeneficiarioMapper beneficiarioMapper;

    public BeneficiarioServiceImpl(BeneficiarioRepository beneficiarioRepository, BeneficiarioMapper beneficiarioMapper) {
        this.beneficiarioRepository = beneficiarioRepository;
        this.beneficiarioMapper = beneficiarioMapper;
    }

    @Override
    public BeneficiarioDTO save(BeneficiarioDTO beneficiarioDTO) {
        log.debug("Request to save Beneficiario : {}", beneficiarioDTO);
        Beneficiario beneficiario = beneficiarioMapper.toEntity(beneficiarioDTO);
        beneficiario = beneficiarioRepository.save(beneficiario);
        return beneficiarioMapper.toDto(beneficiario);
    }

    @Override
    public BeneficiarioDTO update(BeneficiarioDTO beneficiarioDTO) {
        log.debug("Request to update Beneficiario : {}", beneficiarioDTO);
        Beneficiario beneficiario = beneficiarioMapper.toEntity(beneficiarioDTO);
        beneficiario = beneficiarioRepository.save(beneficiario);
        return beneficiarioMapper.toDto(beneficiario);
    }

    @Override
    public Optional<BeneficiarioDTO> partialUpdate(BeneficiarioDTO beneficiarioDTO) {
        log.debug("Request to partially update Beneficiario : {}", beneficiarioDTO);

        return beneficiarioRepository
            .findById(beneficiarioDTO.getId())
            .map(existingBeneficiario -> {
                beneficiarioMapper.partialUpdate(existingBeneficiario, beneficiarioDTO);

                return existingBeneficiario;
            })
            .map(beneficiarioRepository::save)
            .map(beneficiarioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BeneficiarioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Beneficiarios");
        return beneficiarioRepository.findAll(pageable).map(beneficiarioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeneficiarioDTO> findOne(Long id) {
        log.debug("Request to get Beneficiario : {}", id);
        return beneficiarioRepository.findById(id).map(beneficiarioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Beneficiario : {}", id);
        beneficiarioRepository.deleteById(id);
    }
}
