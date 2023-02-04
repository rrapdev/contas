import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../categoria-recebimento.test-samples';

import { CategoriaRecebimentoFormService } from './categoria-recebimento-form.service';

describe('CategoriaRecebimento Form Service', () => {
  let service: CategoriaRecebimentoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CategoriaRecebimentoFormService);
  });

  describe('Service methods', () => {
    describe('createCategoriaRecebimentoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCategoriaRecebimentoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomeCategoria: expect.any(Object),
          })
        );
      });

      it('passing ICategoriaRecebimento should create a new form with FormGroup', () => {
        const formGroup = service.createCategoriaRecebimentoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomeCategoria: expect.any(Object),
          })
        );
      });
    });

    describe('getCategoriaRecebimento', () => {
      it('should return NewCategoriaRecebimento for default CategoriaRecebimento initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCategoriaRecebimentoFormGroup(sampleWithNewData);

        const categoriaRecebimento = service.getCategoriaRecebimento(formGroup) as any;

        expect(categoriaRecebimento).toMatchObject(sampleWithNewData);
      });

      it('should return NewCategoriaRecebimento for empty CategoriaRecebimento initial value', () => {
        const formGroup = service.createCategoriaRecebimentoFormGroup();

        const categoriaRecebimento = service.getCategoriaRecebimento(formGroup) as any;

        expect(categoriaRecebimento).toMatchObject({});
      });

      it('should return ICategoriaRecebimento', () => {
        const formGroup = service.createCategoriaRecebimentoFormGroup(sampleWithRequiredData);

        const categoriaRecebimento = service.getCategoriaRecebimento(formGroup) as any;

        expect(categoriaRecebimento).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICategoriaRecebimento should not enable id FormControl', () => {
        const formGroup = service.createCategoriaRecebimentoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCategoriaRecebimento should disable id FormControl', () => {
        const formGroup = service.createCategoriaRecebimentoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
