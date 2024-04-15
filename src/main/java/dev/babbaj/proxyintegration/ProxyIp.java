package dev.babbaj.proxyintegration;

import com.google.common.collect.Streams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record ProxyIp(String account, List<String> options, String domain) {
    public static List<String> ALL_OPTIONS = List.of(
            "spectate", "force"
    );
    public static Optional<ProxyIp> parseIp(String ip) {
        ip = ip.toLowerCase();
        if (!ip.matches(".+\\.proxy\\..+\\.futureclient\\.gold")) return Optional.empty();
        final String postfix = ip.substring(ip.lastIndexOf("proxy"));
        final String firstPart = ip.substring(0, ip.lastIndexOf(".proxy"));
        final String[] parts = firstPart.split("\\.");
        String account = parts[0];
        List<String> options = List.of(Arrays.copyOfRange(parts, 1, parts.length));
        return Optional.of(new ProxyIp(account, options, postfix));
    }

    public ProxyIp withOption(String option) {
        if (options.contains(option)) return this;
        List<String> newOptions = new ArrayList<>();
        newOptions.add(option);
        newOptions.addAll(this.options);
        return new ProxyIp(account, newOptions, domain);
    }

    public ProxyIp withoutOption(String option) {
        List<String> newOptions = new ArrayList<>(this.options);
        newOptions.remove(option);
        return new ProxyIp(account, newOptions, domain);
    }

    @Override
    public String toString() {
        return Streams.concat(Stream.of(account), options.stream(), Stream.of(domain)).collect(Collectors.joining("."));
    }
}
