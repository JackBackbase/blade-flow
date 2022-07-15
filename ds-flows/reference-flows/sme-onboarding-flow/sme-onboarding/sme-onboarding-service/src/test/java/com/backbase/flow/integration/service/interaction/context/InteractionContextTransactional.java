package com.backbase.flow.integration.service.interaction.context;

/*
This class has been introduced for monkey patching of origin InteractionContextTransactional.class
from DocumentRequestJourney.
Origin class provides interaction context in new transaction, what breaks tests, and need to be explained and probably
resolved in DocumentRequestJourney.
There is a discussion opened with FlowerPower to bring up more context with this transactional context,
then we will decide how this issue can be resolved.

https://stash.backbase.com/projects/FC/repos/deprecated-document-request/pull-requests/106/overview
https://backbase.atlassian.net/browse/FFP-658

At the moment this class is a workaround just to unblock our daily work with IT Tests in SMEO.
*/

import com.backbase.flow.documentstore.FilesetView;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InteractionContextTransactional {

    private final InteractionContext interactionContext;

    public FilesetView getFileset(String filesetName, String tempGroupId) {
        return interactionContext.getFileset(filesetName, tempGroupId);
    }

    public void addFileToFileset(String filesetName, String fileName, InputStream fileData, String tempGroupId) {
        interactionContext.addFileToFileset(filesetName, fileName, fileData, tempGroupId);
    }
}
