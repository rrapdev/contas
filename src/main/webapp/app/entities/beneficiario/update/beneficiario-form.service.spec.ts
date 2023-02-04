import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../beneficiario.test-samples';

import { BeneficiarioFormService } from './beneficiario-form.service';

describe('Beneficiario Form Service', () => {
  let service: BeneficiarioFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BeneficiarioFormService);
  });

  describe('Service methods', () => {
    describe('createBeneficiarioFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBeneficiarioFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomeBeneficiario: expect.any(Object),
            cpfCnpj: expect.any(Object),
          })
        );
      });

      it('passing IBeneficiario should create a new form with FormGroup', () => {
        const formGroup = service.createBeneficiarioFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomeBeneficiario: expect.any(Object),
            cpfCnpj: expect.any(Object),
          })
        );
      });
    });

    describe('getBeneficiario', () => {
      it('should return NewBeneficiario for default Beneficiario initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBeneficiarioFormGroup(sampleWithNewData);

        const beneficiario = service.getBeneficiario(formGroup) as any;

        expect(beneficiario).toMatchObject(sampleWithNewData);
      });

      it('should return NewBeneficiario for empty Beneficiario initial value', () => {
        const formGroup = service.createBeneficiarioFormGroup();

        const beneficiario = service.getBeneficiario(formGroup) as any;

        expect(beneficiario).toMatchObject({});
      });

      it('should return IBeneficiario', () => {
        const formGroup = service.createBeneficiarioFormGroup(sampleWithRequiredData);

        const beneficiario = service.getBeneficiario(formGroup) as any;

        expect(beneficiario).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBeneficiario should not enable id FormControl', () => {
        const formGroup = service.createBeneficiarioFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBeneficiario should disable id FormControl', () => {
        const formGroup = service.createBeneficiarioFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
