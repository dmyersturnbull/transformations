# transformations
A pragmatic and simple Java 8 package that provides:
  * A convenience wrapper for Apache Commons CLI, which has design flaws dating back to 2002.
  * A framework for applying transformations to files and streams.
The framework will apply the transformation on stdin, a single file, or files in a directory matching a filter. Gzip is applied transparently.

Example of using the CLI helper alone:
```java
public static void main(@Nonnull String... args) {

	Optional<ExtendedCommandLine> cli = new CommandLineHelper()
			.setClass(Example.class) // optional; currently only used for help message
			.setHelpHeader("This is an optional header")
			.setHelpFooter("This is an optional footer")
			.addOptions(Option.builder("m").longOpt("multiplier").hasArgs().argName("float").required().build())
			.addOptions(Option.builder("c").longOpt("count").numberOfArgs(Option.UNLIMITED_VALUES).build())
			.addOptions(Option.builder("r").longOpt("restart").desc("Restart from scratch").build())
			.parse(args);

	if (cli.isPresent()) { // true if -h or --help wasn't called, and no error occurred
		boolean restart = cli.get().has("r");
		double multiplier = cli.get().getDouble("m");
		List<Long> counts = cli.get().getListOf("c", Long::new));
	}
}
```

Example of using the transformation framework:
```java
public class Example implements DataTransformation {

	private final String m_text;

	public Example(@Nonnull URL url) throws IOException {
		// store the text read from the url as m_text
		try (InputStream is = url.openStream()) {
			Path tmpFile = Files.createTempFile("dl", "tmp");
			Files.copy(is, tmpFile, StandardCopyOption.REPLACE_EXISTING);
			byte[] encoded = Files.readAllBytes(tmpFile);
			m_text = new String(encoded, StandardCharsets.UTF_8);
		}
	}

	public static void main(@Nonnull String... args) throws Exception {
		new DataTransformationRunner(cli -> new Example(cli.getUrl("u").get()))
				.setClass(Example.class)
				.addOptions(Option.builder("u").longOpt("url").hasArgs().required().build())
				.run(args);
		// the optional arguments -i/--input and -o/--output are added transparently
	}

	@Override
	@Nonnull public FileFilter getApplicableFiles() {
		// if apply(File, File) in the interface is called on directories, each matching file will be transformed
		return f -> f.getName().endsWith(".txt") || f.getName().endsWith(".txt.gz");
	}

	@Override
	public void apply(@Nonnull BufferedReader br, @Nonnull PrintWriter pw) throws IOException {
		br.lines().map(s -> s + m_text).forEach(pw::println);
		// the PrintWriter is subject to an automatic final flush
	}
}
```

### License

The software is licensed under the Apache License, Version 2.0 by [PharmGKB](https://www.pharmgkb.org/) and Douglas Myers-Turnbull.
