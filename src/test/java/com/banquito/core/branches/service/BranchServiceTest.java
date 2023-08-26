package com.banquito.core.branches.service;

import com.banquito.core.branches.exception.CRUDException;
import com.banquito.core.branches.model.Branch;
import com.banquito.core.branches.repository.BranchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BranchServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private BranchService branchService;

    private Branch branch;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        branch = Branch.builder()
                .name("BANQ")
                .code("BANQ123")
                .id("1")
                .build();

    }

    @Test
    void lookById_BranchExists_ReturnBranch() throws CRUDException, CRUDException {

        String id = "1";
        when(branchRepository.findById(id)).thenReturn(Optional.of(branch));

        Branch result = branchService.lookById(id);

        assertNotNull(result);
        assertEquals(branch, result);
    }

    @Test
    void lookById_BranchDoesNotExist_ThrowException() {
        String id = "1";
        when(branchRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CRUDException.class, () -> branchService.lookById(id));
    }

    @Test
    void lookByCode_BranchExists_ReturnBranch() {
        String code = "BANQ123";

        when(branchRepository.findByCode(code)).thenReturn(branch);
        Branch result = branchService.lookByCode(code);
        assertNotNull(result);
        assertEquals(branch, result);
    }

    @Test
    void getAll_ReturnListOfBranches() {
        List<Branch> branches = new ArrayList<>();
        when(branchRepository.findAll()).thenReturn(branches);
        List<Branch> result = branchService.getAll();
        assertNotNull(result);
        assertEquals(branches, result);
    }

    @Test
    void create_ValidBranch() throws CRUDException {

        branchService.create(branch);
        verify(branchRepository).save(branch);
    }


    @Test
    void update_ExistingBranch() throws CRUDException {
        String code = "BANQ1234";
        when(branchRepository.findByCode(code)).thenReturn(branch);
        when((branchRepository).save(branch)).thenReturn(branch);
        assertDoesNotThrow(() -> branchService.update(code, branch));
    }



    @Test
    void update_NonExistingBranch() {

        String code = "BANQ1234";
        when(branchRepository.findByCode(code)).thenReturn(null);
        assertThrows(CRUDException.class, () -> branchService.update(code, branch));
    }

    @Test
    void update_InvalidBranch_ThrowException() {
        String code = "BANQ123";
        when(branchRepository.findByCode(code)).thenReturn(branch);
        doThrow(new RuntimeException()).when(branchRepository).save(branch);
        assertThrows(CRUDException.class, () -> branchService.update(code, branch));
    }
}