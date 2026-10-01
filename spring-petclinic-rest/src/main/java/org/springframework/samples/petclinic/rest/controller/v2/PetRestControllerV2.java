package org.springframework.samples.petclinic.rest.controller.v2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.PetMapper;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.rest.api.PetV2Api;
import org.springframework.samples.petclinic.rest.dto.PetPageDto;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api")
public class PetRestControllerV2 implements PetV2Api {

    private final PetService petService;
    private final PetMapper petMapper;

    public PetRestControllerV2(PetService petService, PetMapper petMapper) {
        this.petService = petService;
        this.petMapper = petMapper;
    }

    @Override
    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    public ResponseEntity<PetPageDto> listPetsPage(Integer page, Integer size) {
        int pageNumber = page == null ? 0 : page;
        int pageSize = size == null ? 20 : size;
        Page<Pet> pets = this.petService.findPets(
            PageRequest.of(pageNumber, pageSize, Sort.by("id")));
        return new ResponseEntity<>(petMapper.toPetPageDto(pets), HttpStatus.OK);
    }

}
