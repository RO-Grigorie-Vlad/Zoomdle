import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LicentaTestModule } from '../../../test.module';
import { ConsultatieUpdateComponent } from 'app/entities/consultatie/consultatie-update.component';
import { ConsultatieService } from 'app/entities/consultatie/consultatie.service';
import { Consultatie } from 'app/shared/model/consultatie.model';

describe('Component Tests', () => {
  describe('Consultatie Management Update Component', () => {
    let comp: ConsultatieUpdateComponent;
    let fixture: ComponentFixture<ConsultatieUpdateComponent>;
    let service: ConsultatieService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LicentaTestModule],
        declarations: [ConsultatieUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ConsultatieUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ConsultatieUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ConsultatieService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Consultatie(123);
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
        const entity = new Consultatie();
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
