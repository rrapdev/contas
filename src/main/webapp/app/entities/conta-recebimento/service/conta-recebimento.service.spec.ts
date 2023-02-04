import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IContaRecebimento } from '../conta-recebimento.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../conta-recebimento.test-samples';

import { ContaRecebimentoService, RestContaRecebimento } from './conta-recebimento.service';

const requireRestSample: RestContaRecebimento = {
  ...sampleWithRequiredData,
  dataVencimento: sampleWithRequiredData.dataVencimento?.format(DATE_FORMAT),
  dataRecebimento: sampleWithRequiredData.dataRecebimento?.format(DATE_FORMAT),
};

describe('ContaRecebimento Service', () => {
  let service: ContaRecebimentoService;
  let httpMock: HttpTestingController;
  let expectedResult: IContaRecebimento | IContaRecebimento[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContaRecebimentoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ContaRecebimento', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const contaRecebimento = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(contaRecebimento).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ContaRecebimento', () => {
      const contaRecebimento = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(contaRecebimento).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ContaRecebimento', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ContaRecebimento', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ContaRecebimento', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addContaRecebimentoToCollectionIfMissing', () => {
      it('should add a ContaRecebimento to an empty array', () => {
        const contaRecebimento: IContaRecebimento = sampleWithRequiredData;
        expectedResult = service.addContaRecebimentoToCollectionIfMissing([], contaRecebimento);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contaRecebimento);
      });

      it('should not add a ContaRecebimento to an array that contains it', () => {
        const contaRecebimento: IContaRecebimento = sampleWithRequiredData;
        const contaRecebimentoCollection: IContaRecebimento[] = [
          {
            ...contaRecebimento,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addContaRecebimentoToCollectionIfMissing(contaRecebimentoCollection, contaRecebimento);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ContaRecebimento to an array that doesn't contain it", () => {
        const contaRecebimento: IContaRecebimento = sampleWithRequiredData;
        const contaRecebimentoCollection: IContaRecebimento[] = [sampleWithPartialData];
        expectedResult = service.addContaRecebimentoToCollectionIfMissing(contaRecebimentoCollection, contaRecebimento);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contaRecebimento);
      });

      it('should add only unique ContaRecebimento to an array', () => {
        const contaRecebimentoArray: IContaRecebimento[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const contaRecebimentoCollection: IContaRecebimento[] = [sampleWithRequiredData];
        expectedResult = service.addContaRecebimentoToCollectionIfMissing(contaRecebimentoCollection, ...contaRecebimentoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contaRecebimento: IContaRecebimento = sampleWithRequiredData;
        const contaRecebimento2: IContaRecebimento = sampleWithPartialData;
        expectedResult = service.addContaRecebimentoToCollectionIfMissing([], contaRecebimento, contaRecebimento2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contaRecebimento);
        expect(expectedResult).toContain(contaRecebimento2);
      });

      it('should accept null and undefined values', () => {
        const contaRecebimento: IContaRecebimento = sampleWithRequiredData;
        expectedResult = service.addContaRecebimentoToCollectionIfMissing([], null, contaRecebimento, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contaRecebimento);
      });

      it('should return initial array if no ContaRecebimento is added', () => {
        const contaRecebimentoCollection: IContaRecebimento[] = [sampleWithRequiredData];
        expectedResult = service.addContaRecebimentoToCollectionIfMissing(contaRecebimentoCollection, undefined, null);
        expect(expectedResult).toEqual(contaRecebimentoCollection);
      });
    });

    describe('compareContaRecebimento', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareContaRecebimento(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareContaRecebimento(entity1, entity2);
        const compareResult2 = service.compareContaRecebimento(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareContaRecebimento(entity1, entity2);
        const compareResult2 = service.compareContaRecebimento(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareContaRecebimento(entity1, entity2);
        const compareResult2 = service.compareContaRecebimento(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
