import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LicentaTestModule } from '../../../test.module';
import { AplicareLicentaUpdateComponent } from 'app/entities/aplicare-licenta/aplicare-licenta-update.component';
import { AplicareLicentaService } from 'app/entities/aplicare-licenta/aplicare-licenta.service';
import { AplicareLicenta } from 'app/shared/model/aplicare-licenta.model';

describe('Component Tests', () => {
  describe('AplicareLicenta Management Update Component', () => {
    let comp: AplicareLicentaUpdateComponent;
    let fixture: ComponentFixture<AplicareLicentaUpdateComponent>;
    let service: AplicareLicentaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LicentaTestModule],
        declarations: [AplicareLicentaUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(AplicareLicentaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AplicareLicentaUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AplicareLicentaService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new AplicareLicenta(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new AplicareLicenta();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
