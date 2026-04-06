package com.artspire.backend.config;

import io.supabase.SupabaseClient;

public class SupabaseConfig {

    private static SupabaseConfig instance;
    private SupabaseClient client;

    private SupabaseConfig() {
        // initialize your supabase client here
        client = new SupabaseClient(
                "https://mqyraqvcomlmvwfqqvbw.supabase.co",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1xeXJhcXZjb21sbXZ3ZnFxdmJ3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzQ2ODMwOTgsImV4cCI6MjA5MDI1OTA5OH0.yL9ZLNVEE1qj-WuXALSNkijTh0SShjP8zz5tZ-l9h40"
        );
    }

    public static SupabaseConfig getInstance() {
        if (instance == null) {
            instance = new SupabaseConfig();
        }
        return instance;
    }

    public SupabaseClient getClient() {
        return client;
    }
}