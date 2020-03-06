import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { LicentaTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { AplicareConsultatieDeleteDialogComponent } from 'app/entities/aplicare-consultatie/aplicare-consultatie-delete-dialog.component';
import { AplicareConsultatieService } from 'app/entities/aplicare-consultatie/aplicare-consultatie.service';

describe('Component Tests', () => {
  describe('AplicareConsultatie Management Delete Component', () => {
    let comp: AplicareConsultatieDeleteDialogComponent;
    let fixture: ComponentFixture<AplicareConsultatieDeleteDialogComponent>;
    let service: AplicareConsultatieService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LicentaTestModule],
        declarations: [AplicareConsultatieDeleteDialogComponent]
      })
        .overrideTemplate(AplicareConsultatieDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AplicareConsultatieDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AplicareConsultatieService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
