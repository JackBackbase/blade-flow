import { DocumentRequestComponent } from './document-request.component';
import { DocumentRequestStatus } from '@backbase/ac-common-ang';

describe('DocumentRequestComponent', () => {
  let component: DocumentRequestComponent;

  beforeEach(() => {
    component = new DocumentRequestComponent();
    component.documentRequest = {
      deadline: '2020-06-04T06:49:42.019Z',
      documentType: 'Driver license',
      externalId: 'DRQ-101',
      filesetName: 'Driver license',
      groupId: '7b37e819-f98a-4930-8df1-7293ffe471ef',
      initialNote: 'NL driver license only.',
      internalId: '71976a3f-36d8-4da9-a1b3-5bf4321a36be',
      processInstanceId: 'f5d46278-cca8-11ea-9887-448a5b8c66ba',
      referenceId: 'MOR-2348',
      status: DocumentRequestStatus.Open,
      creator: {
        fullName: 'John Doe',
        userId: '123',
      },
    };
  });

  it('should be "actionable"', () => {
    component.documentRequest.status = DocumentRequestStatus.Open;
    component.ngOnChanges();
    expect(component.actionable).toEqual(true);

    component.documentRequest.status = DocumentRequestStatus.Pending;
    component.ngOnChanges();
    expect(component.actionable).toEqual(false);
  });
});
