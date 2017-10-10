package ua.in.smartjava;

public class SimpsonLoaderFactory {

    SimpsonLoader getSimpsonLoader(LoaderType loaderType) {
        switch (loaderType) {
            case SECURED:
                return SimpsonXmlLoader.builder().secured(true).build();
            case ACCUMULATED:
                return SimpsonXmlLoader.builder().increaseAccumulatedSize(true).build();
            default:
                return SimpsonXmlLoader.builder().build();
        }
    }

    public enum LoaderType {
        INSECURED, SECURED, ACCUMULATED
    }
}
