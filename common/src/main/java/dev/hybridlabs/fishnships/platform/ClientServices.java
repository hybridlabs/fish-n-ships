package dev.hybridlabs.fishnships.platform;

import static dev.hybridlabs.fishnships.platform.Services.load;

import dev.hybridlabs.fishnships.platform.services.ClientPlatformHelper;

public class ClientServices {
    public static final ClientPlatformHelper RENDERER = load(ClientPlatformHelper.class);
}
