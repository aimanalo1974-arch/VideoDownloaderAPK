if (success) {

    statusText.setText(

        "DOWNLOAD COMPLETE! ✅\n\n" +

        "File:\n" +
        downloadedFile +

        "\n\nSaved to:\n" +
        folder +

        "\n\n▶ CLICK HERE TO WATCH VIDEO"

    );


    // Make the message clickable
    statusText.setClickable(true);


    statusText.setOnClickListener(v -> {

        try {

            File videoFile = new File(

                "/storage/emulated/0/Download/" +
                downloadedFile

            );


            Uri videoUri = FileProvider.getUriForFile(

                MainActivity.this,

                getPackageName() + ".fileprovider",

                videoFile

            );


            Intent intent = new Intent(

                Intent.ACTION_VIEW

            );


            intent.setDataAndType(

                videoUri,

                "video/*"

            );


            intent.addFlags(

                Intent.FLAG_GRANT_READ_URI_PERMISSION

            );


            startActivity(

                Intent.createChooser(

                    intent,

                    "Watch Video"

                )

            );


        } catch (Exception e) {

            Toast.makeText(

                MainActivity.this,

                "Cannot open video: " +
                e.getMessage(),

                Toast.LENGTH_LONG

            ).show();

        }

    });

}
